package com.xiaoxiz.xiaomaibu.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.xiaoxiz.xiaomaibu.bean.Order;
import com.xiaoxiz.xiaomaibu.bean.Refund;
import com.xiaoxiz.xiaomaibu.service.OrderService;
import com.xiaoxiz.xiaomaibu.service.RefundService;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/alipay")
public class AlipayController {
    @Autowired
    OrderService orderService;
    @Autowired
    RefundService refundService;

    @PostMapping("/pay")
    public DataResult alipayment(@RequestBody List<String> order_noList) {

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,
                AlipayConfig.APP_ID, AlipayConfig.APP_PRIVATE_KEY, "json",
                AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,
                AlipayConfig.sign_type);
        String out_trade_no = UUID.randomUUID().toString().replace("-","");
        System.out.println("商户订单号" + out_trade_no);
        float sum = 0;
        for (int i=0;i<order_noList.size();i++){
            String order_no = order_noList.get(i);
            Order order =orderService.find(order_no);
            order.setOut_trade_no(out_trade_no);
            orderService.update(order);
            float total = order.getTotal();
            sum+=total;
        }
        //构建支付宝请求参数
        String payMoney = String.valueOf(sum);
        AlipayTradeAppPayRequest request = AliRequestParam.startRequestAli(payMoney, out_trade_no);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，客户端可以直接发起支付，无需再做处理。
            @SuppressWarnings("rawtypes")
            Map map = new HashMap();
            map.put("orderString", response.getBody());
            //这里做生成订单操作，订单状态为未支付
            return DataResult.success(map);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return DataResult.ERROR();


    }

    @PostMapping("/refund/{orderno}")
    public String refund(@PathVariable String orderno) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl,AlipayConfig.APP_ID
                ,AlipayConfig.APP_PRIVATE_KEY,"json",AlipayConfig.CHARSET
                ,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.sign_type);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        Order order=orderService.find(orderno); //查询订单
        System.out.println(order.toString());
        String refundAmount = String.valueOf(order.getTotal());//查找退款的金额
        AlipayTradeRefundModel refundModel =new AlipayTradeRefundModel();
        refundModel.setOutTradeNo(order.getOut_trade_no());//商家订单号
        refundModel.setTradeNo("");
        refundModel.setRefundAmount(refundAmount);//退款金额
        refundModel.setOutRequestNo(orderno);//要退款的订单
        request.setBizModel(refundModel);

//// 返回参数选项，按需传入
//JSONArray queryOptions = new JSONArray();
//queryOptions.add("refund_detail_item_list");
//bizContent.put("query_options", queryOptions);


        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            List<Refund> refundList=refundService.getRefund(orderno);
            Refund refund=refundList.get(0);
            refund.setStatus("1");
            JSONObject jsonObject = JSON.parseObject(response.getBody());
            JSONObject jsonObject1 = jsonObject.getJSONObject("alipay_trade_refund_response");
            String time = jsonObject1.getString("gmt_refund_pay");
            System.out.println(time);
            refund.setTime(time);
            refundService.update(refund);
        } else {
            System.out.println("调用失败");
        }
        return response.getBody();
    }
    //异步通知
    @PostMapping("/pay_notify")
    public void notifyUrl(HttpServletResponse response, HttpServletRequest request) throws IOException, AlipayApiException {
        System.out.println("异步通知");
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("utf-8");//乱码解决，这段代码在出现乱码时使用
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String str : requestParams.keySet()) {
            String name = str;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2"); //调用SDK验证签名

        if (!signVerified) {
            System.out.println("验签失败");
            out.print("fail");
            return;
        }

        //商户订单号,之前生成的带用户ID的订单号
        String out_trade_no = params.get("out_trade_no");
        //支付宝交易号
        String trade_no = params.get("trade_no");
        //付款金额
        String total_amount = params.get("total_amount");
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

//        String appId=params.get("app_id");//支付宝分配给开发者的应用Id
//        String notifyTime=params.get("notify_time");//通知时间:yyyy-MM-dd HH:mm:ss
//        String gmtCreate=params.get("gmt_create");//交易创建时间:yyyy-MM-dd HH:mm:ss
        String gmtPayment = params.get("gmt_payment");//交易付款时间
//        String gmtRefund=params.get("gmt_refund");//交易退款时间
//        String gmtClose=params.get("gmt_close");//交易结束时间
//        String tradeNo=params.get("trade_no");//支付宝的交易号
//        String outTradeNo = params.get("out_trade_no");//获取商户之前传给支付宝的订单号（商户系统的唯一订单号）
//        String outBizNo=params.get("out_biz_no");//商户业务号(商户业务ID，主要是退款通知中返回退款申请的流水号)
//        String buyerLogonId=params.get("buyer_logon_id");//买家支付宝账号
//        String sellerId=params.get("seller_id");//卖家支付宝用户号
//        String sellerEmail=params.get("seller_email");//卖家支付宝账号
//        String totalAmount=params.get("total_amount");//订单金额:本次交易支付的订单金额，单位为人民币（元）
//        String receiptAmount=params.get("receipt_amount");//实收金额:商家在交易中实际收到的款项，单位为元
//        String invoiceAmount=params.get("invoice_amount");//开票金额:用户在交易中支付的可开发票的金额
//        String buyerPayAmount=params.get("buyer_pay_amount");//付款金额:用户在交易中支付的金额
//        String tradeStatus = params.get("trade_status");// 获取交易状态


        if (trade_status.equals("TRADE_FINISHED")) {
            /*此处可自由发挥*/
            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序
            //注意：
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        } else if (trade_status.equals("TRADE_SUCCESS")) {
            //这里就可以更新订单状态为已支付啦
            List<Order> orderList=orderService.out_trade_no(out_trade_no);
            for (Order order:
                 orderList) {
                order.setPaytime(gmtPayment);
                order.setStatus(1);
                orderService.update(order);
            }
            System.out.println("成功支付");
        }
        out.print("success");
    }
}
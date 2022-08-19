package com.xiaoxiz.xiaomaibu.alipay;

import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;

public class AliRequestParam {
    public static AlipayTradeAppPayRequest startRequestAli(String payMoney, String trade_no) {
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("某公司");
        model.setSubject("某商品");
        model.setOutTradeNo(trade_no);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(payMoney);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("http://72ep97.natappfree.cc/alipay/pay_notify");//这里要注意的是，这个地址外网必须要能访问，不然服务端无法接收到异步通知的，你只需要把example.com改成你的ip地址和端口号即可，如果做了端口映射的话就只填ip地址即可
        return request;
    }
}

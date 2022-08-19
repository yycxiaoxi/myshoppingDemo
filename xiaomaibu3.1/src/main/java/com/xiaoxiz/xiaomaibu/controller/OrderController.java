package com.xiaoxiz.xiaomaibu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xiaoxiz.xiaomaibu.bean.Ddanxiz;
import com.xiaoxiz.xiaomaibu.bean.Goods;
import com.xiaoxiz.xiaomaibu.bean.Order;
import com.xiaoxiz.xiaomaibu.bean.Refund;
import com.xiaoxiz.xiaomaibu.service.DdanxizService;
import com.xiaoxiz.xiaomaibu.service.GoodsService;
import com.xiaoxiz.xiaomaibu.service.OrderService;
import com.xiaoxiz.xiaomaibu.service.RefundService;
import com.xiaoxiz.xiaomaibu.util.JWTUtils;
import com.xiaoxiz.xiaomaibu.util.dataresult.DataResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    DdanxizService ddanxizService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RefundService refundService;

    /**
     * 创建订单请求
     * @param orderMap
     * @return
     */
    @PostMapping("/insert")
    public DataResult insertOrderAndDdanxiz(@RequestBody Map<String,Object> orderMap){
        String orderJson =JSON.toJSONString(orderMap.get("order"));
        String ddanxizJson=JSONObject.toJSONString(orderMap.get("ddanxiz"));
        Order order = JSONObject.parseObject(orderJson,Order.class);
        //插入订单
        orderService.insert(order);
        List<Ddanxiz> ddanxizList = JSONObject.parseObject(ddanxizJson,new TypeReference<List<Ddanxiz>>(){});
        for (Ddanxiz ddanxiz:
             ddanxizList) {
            ddanxizService.insert(ddanxiz);
        }
        return DataResult.success();
    }

    /**
     * 未付款拉取数据请求
     * @param request
     * @return
     */
    @PostMapping("/noPay")
    public DataResult selectOrder_noPay(HttpServletRequest request){
        String JWT = request.getHeader("Authorization");
        DecodedJWT decodedJWT = JWTUtils.decodeRsa(JWT);
        // 2.取出JWT字符串载荷中的随机token，从Redis中获取用户信息
        String user_id = decodedJWT.getClaim("user_id").asString();
        //订单列表
        List<Order> orderList=orderService.findallnopay(user_id);
        Map<String,Object> objectMap = getObjectMap(orderList);

        return DataResult.success(objectMap);
    }

    /**
     * 取消订单请求
     * @return
     */
    @PostMapping("/cancelOrder/{order_no}")
    public DataResult cancelOrder(HttpServletRequest request, @PathVariable String order_no){
        String JWT = request.getHeader("Authorization");
        DecodedJWT decodedJWT = JWTUtils.decodeRsa(JWT);
        // 2.取出JWT字符串载荷中的随机token，从Redis中获取用户信息
        String user_id = decodedJWT.getClaim("user_id").asString();
        Order order = orderService.find(order_no);
        if (order.getId().equals(user_id)){//验证是否为该用户订单
            ddanxizService.delete(order_no);
            orderService.delete(order_no);
        }
        return  DataResult.success();
    }

    /**
     * 查看订单状态
     * 未发货 status=1
     * 待收货 status=2
     * 待评价 status=3
     * @param request
     * @param status
     * @return
     */
    @PostMapping("/orderStatus/{status}")
    public DataResult daifahuo(HttpServletRequest request,@PathVariable String status){
        String JWT = request.getHeader("Authorization");
        DecodedJWT decodedJWT = JWTUtils.decodeRsa(JWT);
        // 2.取出JWT字符串载荷中的随机token，从Redis中获取用户信息
        String user_id = decodedJWT.getClaim("user_id").asString();
        List<Order> orderList = new ArrayList<>();
        switch (status){
            case "1":
                orderList=orderService.daifahuo(user_id);
                break;
            case "2":
                orderList=orderService.daishouhuo(user_id);
                break;
            case "3":
                orderList=orderService.daishouhuo(user_id);
                break;
            case "4":
                orderList=orderService.userdaipingjia(user_id);
        }

        Map<String,Object> objectMap = getObjectMap(orderList);
        return DataResult.success(objectMap);

    }
    /**
     *设置收货
     */
    @PostMapping("/shouHuo/{order_no}")
    public DataResult shouHuo(@PathVariable String order_no){
        Order order= orderService.find(order_no);
        order.setStatus(4);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String shouHuoTime  =  df.format(new Date());// new Date()为获取当前系统时间
        order.setAgrtime(shouHuoTime);
        orderService.update(order);
        return  DataResult.success();
    }
    @PostMapping()
    public DataResult daiPingJia(){
        return DataResult.success();
    }

    //找用户已支付的订单
    @GetMapping("/admin/daifahuo")
    public DataResult daifahuoAll(){
        List<Order> orderList=orderService.daifahuoAll();
        return DataResult.success(getObjectMap(orderList));
    }
    //找到已发货的订单
    @GetMapping("/admin/daishouhuo")
    public DataResult daishouhuo(){
        List<Order> orderList = orderService.daishouhuoAll();
        return DataResult.success(getObjectMap(orderList));
    }

    @PostMapping("/admin/update")
    public DataResult orderUpdate(@RequestBody Order order){
        orderService.update(order);
        System.out.println(order.toString());
        return DataResult.success();
    }

    /**
     * 用户拉去退款申请
     * @param request
     * @return
     */
    @GetMapping("/getRefund")
    public DataResult getRefund(HttpServletRequest request){
        String JWT = request.getHeader("Authorization");
        DecodedJWT decodedJWT = JWTUtils.decodeRsa(JWT);
        // 2.取出JWT字符串载荷中的随机token，从Redis中获取用户信息
        String user_id = decodedJWT.getClaim("user_id").asString();
        List<Order> orderList = orderService.userrefund(user_id);
        Map <String,Object> objectMap = getObjectMap(orderList);
        List<Refund> refundList = new ArrayList<>();
        for (Order order:
             orderList) {
            refundList.addAll(refundService.getRefund(order.getOrderno()));
        }
        objectMap.put("refundList",refundList);
        return DataResult.success(objectMap);
    }

    /**
     * 商家拉取退款申请
     * @param
     * @return
     */
    @GetMapping("/refundall")
    public DataResult getRefundALL(){

        List<Order> orderList=orderService.refundAll();
        Map <String,Object> objectMap = getObjectMap(orderList);
        List<Refund> refundList = new ArrayList<>();
        for (Order order:
                orderList) {
            refundList.addAll(refundService.getRefund(order.getOrderno()));
        }
        objectMap.put("refundList",refundList);
        return DataResult.success(objectMap);

    }
    //内部方法获取返回的订单列表
    private Map<String,Object> getObjectMap(List<Order> orderList){

        //订单详情列表
        List<Ddanxiz> ddanxizList =new ArrayList<>();
        for (int i=0;i<orderList.size();i++){
            ddanxizList.addAll(ddanxizService.findall(orderList.get(i).getOrderno()));
        }
        //商品信息map
        Map<String,Goods> goodsMap = new HashMap<>();
        for (int i=0;i<ddanxizList.size();i++){
            String gid=ddanxizList.get(i).getGid();
            Goods goods = goodsService.find(gid);
            goodsMap.put(gid,goods);
        }
        //构造没有重复gid的商品信息列表
        List<Goods> goodsList = new ArrayList<>();
        for (String key: goodsMap.keySet()){
            Goods goods=goodsMap.get(key);
            goodsList.add(goods);
        }
        //将3个列表传给客户端进行处理
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("orderList",orderList);
        objectMap.put("ddanxizList",ddanxizList);
        objectMap.put("goodsList",goodsList);
        return objectMap;
    }


}


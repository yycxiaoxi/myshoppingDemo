package com.xiaoxiz.xiaomaibu.service;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.xiaoxiz.xiaomaibu.bean.Order;

import java.util.List;

public interface OrderService {
    public void insert(Order order);
    public void delete(String orderno);
    public void update(Order order);
    public Order find(String orderno);
    public List<Order> findallnopay(String uid);
    public List<Order> daifahuo(String uid);
    public List<Order> daishouhuo(String uid);
    public List<Order> userdaishouhuo(String uid);
    public List<Order> userdaipingjia(String uid);
    public List<Order> userrefund(String uid);
    public List<Order> out_trade_no(String out_trade_no);
    public List<Order> daifahuoAll();
    public List<Order> daishouhuoAll();
    public List<Order> refundAll();


}

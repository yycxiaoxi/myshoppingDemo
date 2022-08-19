package com.xiaoxiz.xiaomaibu.dao;

import com.xiaoxiz.xiaomaibu.bean.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderDao {
    public void insert(Order order);
    public void delete(String orderno);
    public void update(Order order);
    public Order find(String orderno);
    public List<Order> findallnopay(String uid);//status = 0
    public List<Order> daifahuo(String uid);//status = 1
    public List<Order> daishouhuo(String uid);//status = 2
    public List<Order> userdaishouhuo(String uid);// status = 3
    public List<Order> userdaipingjia(String uid);// status = 4
    public List<Order> userrefund(String uid);// status = 5
    public List<Order>  out_trade_no(String out_trade_no);//获取付款编号
    public List<Order> daifahuoAll();
    public List<Order> daishouhuAll();
    public List<Order> refundAll();
}

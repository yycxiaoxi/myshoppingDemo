package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.bean.Order;
import com.xiaoxiz.xiaomaibu.dao.OrderDao;
import com.xiaoxiz.xiaomaibu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;
    @Override
    public void insert(Order order) {
        orderDao.insert(order);
    }

    @Override
    public void delete(String orderno) {
        orderDao.delete(orderno);
    }

    @Override
    public void update(Order order) {
        orderDao.update(order);
    }

    @Override
    public Order find(String orderno) {

        return orderDao.find(orderno);
    }

    @Override
    public List<Order> findallnopay(String uid) {
        return orderDao.findallnopay(uid);
    }

    @Override
    public List<Order> daifahuo(String uid) {
        return orderDao.daifahuo(uid);
    }

    @Override
    public List<Order> daishouhuo(String uid) {
        return orderDao.daishouhuo(uid);
    }

    @Override
    public List<Order> userdaishouhuo(String uid) {

        return orderDao.userdaishouhuo(uid);
    }

    @Override
    public List<Order> userdaipingjia(String uid) {

        return orderDao.userdaipingjia(uid);
    }

    @Override
    public List<Order> userrefund(String uid) {
        return orderDao.userrefund(uid);
    }



    @Override
    public List<Order> out_trade_no(String out_trade_no) {
        return orderDao.out_trade_no(out_trade_no);
    }

    @Override
    public List<Order> daifahuoAll() {
        return orderDao.daifahuoAll();
    }

    @Override
    public List<Order> daishouhuoAll() {
        return orderDao.daishouhuAll();
    }

    @Override
    public List<Order> refundAll() {
        return orderDao.refundAll();
    }
}

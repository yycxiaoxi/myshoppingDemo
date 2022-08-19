package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.bean.Refund;
import com.xiaoxiz.xiaomaibu.dao.RefundDao;
import com.xiaoxiz.xiaomaibu.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RefundServiceImpl implements RefundService {
    @Autowired
    RefundDao refundDao;
    @Override
    public void insert(Refund refund) {
        refundDao.insert(refund);
    }

    @Override
    public List<Refund> getRefund(String orderno) {
        return refundDao.getRefund(orderno);
    }


    @Override
    public void update(Refund refund) {
        refundDao.update(refund);
    }

    @Override
    public void delete(String orderno) {
        refundDao.delete(orderno);
    }
}

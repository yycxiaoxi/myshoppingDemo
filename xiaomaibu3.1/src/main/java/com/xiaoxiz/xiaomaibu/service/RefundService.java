package com.xiaoxiz.xiaomaibu.service;

import com.xiaoxiz.xiaomaibu.bean.Refund;
import org.springframework.stereotype.Service;

import java.util.List;


public interface RefundService {
    public void insert(Refund refund);
    public List<Refund> getRefund(String orderno);
    public void update(Refund refund);
    public void delete(String orderno);
}

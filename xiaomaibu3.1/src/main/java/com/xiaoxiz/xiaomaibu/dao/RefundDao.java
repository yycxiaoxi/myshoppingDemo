package com.xiaoxiz.xiaomaibu.dao;

import com.xiaoxiz.xiaomaibu.bean.Refund;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RefundDao {
    public void insert(Refund refund);
    public List<Refund> getRefund(String orderno);
    public void update(Refund refund);
    public void delete(String orderno);
}

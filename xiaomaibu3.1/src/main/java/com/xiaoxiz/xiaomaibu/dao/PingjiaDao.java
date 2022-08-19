package com.xiaoxiz.xiaomaibu.dao;

import com.xiaoxiz.xiaomaibu.bean.Pingjia;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PingjiaDao {
    public Pingjia findEva(String eva_no);
    public void insertEva(Pingjia pingjia);
    public void delEva(String pingjiano);
    public List<Pingjia> findGEva(String gid);
    public Pingjia findEvaByOrder(String xizehao);
    public void updateEva(Pingjia pingjia);
}

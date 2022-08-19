package com.xiaoxiz.xiaomaibu.service;

import com.xiaoxiz.xiaomaibu.bean.Pingjia;

import java.util.List;

public interface PingjiaService {
    public Pingjia findEva(String eva_no);
    public void insertEva(Pingjia pingjia);
    public void delEva(String pingjiano);
    public List<Pingjia> findGEva(String gid);
    public Pingjia findEvaByOrder(String xizehao);
    public void updateEva(Pingjia pingjia);
}

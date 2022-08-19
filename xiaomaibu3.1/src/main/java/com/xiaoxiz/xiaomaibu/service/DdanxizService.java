package com.xiaoxiz.xiaomaibu.service;

import com.xiaoxiz.xiaomaibu.bean.Ddanxiz;

import java.util.List;

public interface DdanxizService {
    public void insert(Ddanxiz ddanxiz);
    public List<Ddanxiz> findall(String order_no);
    public void delete(String order_no);
    public Ddanxiz find(String xizehao);
    public void update(Ddanxiz ddanxiz);
}

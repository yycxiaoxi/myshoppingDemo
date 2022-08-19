package com.xiaoxiz.xiaomaibu.service;

import com.xiaoxiz.xiaomaibu.bean.Goods;

import java.util.List;

public interface GoodsService {
    public void insert(Goods goods);
    public List<Goods> findall();
    public Goods find(String gid);
    public void update (Goods goods);
    public void delete(String gid);
    public List<Goods> findLike(String like);
    public List<Goods> findSort(String sort);
}

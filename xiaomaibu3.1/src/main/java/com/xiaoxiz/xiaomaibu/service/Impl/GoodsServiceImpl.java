package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.bean.Goods;
import com.xiaoxiz.xiaomaibu.dao.GoodsDao;
import com.xiaoxiz.xiaomaibu.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Override
    public void insert(Goods goods) {
        goodsDao.insert(goods);
    }

    @Override
    public List<Goods> findall() {
        return goodsDao.findall();
    }

    @Override
    public Goods find(String gid) {
        return goodsDao.find(gid);
    }

    @Override
    public void update(Goods goods) {
        goodsDao.update(goods);
    }

    @Override
    public void delete(String gid) {
        goodsDao.delete(gid);
    }

    @Override
    public List<Goods> findLike(String like) {
        return goodsDao.findLike(like);
    }

    @Override
    public List<Goods> findSort(String sort) {
        return goodsDao.findSort(sort);
    }
}

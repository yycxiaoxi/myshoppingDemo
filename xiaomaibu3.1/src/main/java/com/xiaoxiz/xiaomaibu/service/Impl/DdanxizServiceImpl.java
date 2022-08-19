package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.bean.Ddanxiz;
import com.xiaoxiz.xiaomaibu.dao.DdanxizDao;
import com.xiaoxiz.xiaomaibu.service.DdanxizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DdanxizServiceImpl implements DdanxizService {
    @Autowired
    DdanxizDao ddanxizDao;
    @Override
    public void insert(Ddanxiz ddanxiz) {
        ddanxizDao.insert(ddanxiz);
    }

    @Override
    public List<Ddanxiz> findall(String order_no) {
        return ddanxizDao.findall(order_no);
    }

    @Override
    public void delete(String order_no) {
        ddanxizDao.delete(order_no);
    }

    @Override
    public Ddanxiz find(String xizehao) {
        return ddanxizDao.find(xizehao);
    }

    @Override
    public void update(Ddanxiz ddanxiz) {
        ddanxizDao.update(ddanxiz);
    }
}

package com.xiaoxiz.xiaomaibu.service.Impl;

import com.xiaoxiz.xiaomaibu.bean.Pingjia;
import com.xiaoxiz.xiaomaibu.dao.PingjiaDao;
import com.xiaoxiz.xiaomaibu.service.PingjiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EvaluateServiceImpl implements PingjiaService {
    @Autowired
    PingjiaDao pingjiaDao;
    @Override
    public Pingjia findEva(String eva_no) {
        return pingjiaDao.findEva(eva_no);
    }

    @Override
    public void insertEva(Pingjia pingjia) {
        pingjiaDao.insertEva(pingjia);
    }



    @Override
    public void delEva(String pingjiano) {
        pingjiaDao.delEva(pingjiano);
    }

    @Override
    public List<Pingjia> findGEva(String gid) {
        return pingjiaDao.findGEva(gid);
    }

    @Override
    public Pingjia findEvaByOrder(String xizehao) {
        return pingjiaDao.findEvaByOrder(xizehao);
    }

    @Override
    public void updateEva(Pingjia pingjia) {
        pingjiaDao.updateEva(pingjia);
    }
}

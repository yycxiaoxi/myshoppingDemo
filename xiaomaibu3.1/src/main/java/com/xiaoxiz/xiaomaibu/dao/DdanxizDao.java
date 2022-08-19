package com.xiaoxiz.xiaomaibu.dao;

import com.xiaoxiz.xiaomaibu.bean.Ddanxiz;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DdanxizDao {
    public void insert(Ddanxiz ddanxiz);
    public List<Ddanxiz> findall(String order_no);
    public void delete(String order_no);
    public Ddanxiz find(String xizehao);
    public void update(Ddanxiz ddanxiz);
}

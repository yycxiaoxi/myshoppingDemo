package com.xiaoxiz.xiaomaibu.dao;

import com.xiaoxiz.xiaomaibu.bean.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsDao {
    public void insert(Goods goods);
    public List<Goods> findall();
    public Goods find(String gid);
    public void update (Goods goods);
    public void delete(String gid);
    public List<Goods> findLike(String like);
    public List<Goods> findSort(String sort);

}

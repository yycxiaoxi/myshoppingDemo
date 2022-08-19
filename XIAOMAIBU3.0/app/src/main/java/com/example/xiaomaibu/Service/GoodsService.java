package com.example.xiaomaibu.Service;

import android.content.Context;

import com.example.xiaomaibu.Bean.Goods;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GoodsService {
    public void insert(Goods goods, Context context);
    public List<Goods> findall(Context context);
    public Goods find(String gid,Context context);
    public void update (Goods goods,Context context);
    public void delete(String gid,Context context);
    public List<Goods> findLike(String like,Context context);
    public List<Goods> findSort(String sort,Context context);
    public List<Goods> find(List<String> gidList, Context context) throws IOException;
}

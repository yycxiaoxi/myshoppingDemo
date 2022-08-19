package com.example.xiaomaibu.Service.Impl;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Service.GoodsService;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GoodsServiceImpl implements GoodsService {


    @Override
    public void insert(Goods goods, Context context) {
        
    }

    @Override
    public List<Goods> findall(Context context) {
        return null;
    }

    @Override
    public Goods find(String gid, Context context) {
        return null;
    }

    @Override
    public void update(Goods goods, Context context) {

    }

    @Override
    public void delete(String gid, Context context) {

    }

    @Override
    public List<Goods> findLike(String like, Context context) {
        return null;
    }

    @Override
    public List<Goods> findSort(String sort, Context context) {
        return null;
    }

    @Override
    public List<Goods> find(List<String> gidList, Context context) throws IOException {

        String json= JSON.toJSONString(gidList);
        String body=OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/goods/find/cart", json, context);
        JSONArray jsonArray=OkhttpUtils.getData(body);
        List<Goods> goodsList = JSON.parseObject(jsonArray.toJSONString(),
                new TypeReference<List<Goods>>(){});
        return goodsList;
    }
}

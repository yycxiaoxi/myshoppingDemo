package com.example.xiaomaibu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.PingJiaAdapter;
import com.example.xiaomaibu.Adapter.daifahuoAdapter;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.GoodsInfoActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PingJiaActivity extends AppCompatActivity {
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private List<Order> orderList;
    private List<Ddanxiz> ddanxizList;
    private List<Goods> goodsList;
    private ExpandableListView expandableListView;
    private String uid;
    private Context mContext;
    private PingJiaAdapter pingJiaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_jia);
        expandableListView=findViewById(R.id.expandableListView);
        mContext=PingJiaActivity.this;
        //判断等登录？
        TemploginDao temploginDao=new TemploginDao();
        uid=temploginDao.find(PingJiaActivity.this);
        if(uid==null){
            Intent intent=new Intent(PingJiaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        try {
            OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/orderStatus/4", "", mContext, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body = response.body().string();
                    String status=OkhttpUtils.getCode(body);
                    if ("200".equals(status)) {
                        JSONObject jsonObject = (JSONObject) JSON.parseObject(body).get("data");
                        Log.e("body", jsonObject.toJSONString());
                        JSONArray ddanxizListString = jsonObject.getJSONArray("ddanxizList");
                        JSONArray goodsListString = jsonObject.getJSONArray("goodsList");
                        JSONArray orderListString = jsonObject.getJSONArray("orderList");
                        ddanxizList = JSON.parseObject(ddanxizListString.toJSONString(), new TypeReference<List<Ddanxiz>>() {
                        });
                        goodsList = JSON.parseObject(goodsListString.toJSONString(), new TypeReference<List<Goods>>() {
                        });
                        orderList = JSON.parseObject(orderListString.toJSONString(), new TypeReference<List<Order>>() {
                        });
                        PingJiaActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (orderList.size()!=0){
                                    //初始化数据
                                    initData();
                                    pingJiaAdapter=new PingJiaAdapter(PingJiaActivity.this,mContext,pdata,mdata,uid);
                                    expandableListView.setAdapter(pingJiaAdapter);
                                    expandableListView.expandGroup(0);
                                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                        @Override
                                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                            String gid=(String) mdata.get(groupPosition).get(childPosition).get("gid");
                                            Intent intent = new Intent(PingJiaActivity.this, GoodsInfoActivity.class);
                                            intent.putExtra("gid",gid);
                                            startActivity(intent);
                                            return true;
                                        }
                                    });

                                }
                            }
                        });
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void initData(){

        mdata=new ArrayList<>();
        pdata=new ArrayList<>();
        for(int i=0;i<orderList.size();i++){
            List<Map<String,Object>> mapList=new ArrayList<>();
            Map pmap=new HashMap();
            pmap.put("status",orderList.get(i).getStatus());
            pmap.put("orderno",orderList.get(i).getOrderno());
            pmap.put("createtime",orderList.get(i).getAgrtime());
            pmap.put("total",orderList.get(i).getTotal());
            for(int j=0;j<ddanxizList.size();j++){
                if(ddanxizList.get(j).getOrderno().equals(orderList.get(i).getOrderno())){//找对应的订单的细则
                    Map<String,Object> map=new HashMap<>();
                    map.put("xizehao",ddanxizList.get(i).getGid());
                    for (int k=0;k<goodsList.size();k++){//找对应的商品号
                        if(ddanxizList.get(j).getGid().equals(goodsList.get(k).getGid())){
                            map.put("name",goodsList.get(k).getName());
                            map.put("img",goodsList.get(k).getImg());
                            map.put("gid",goodsList.get(k).getGid());
                            break;
                        }
                    }
                    map.put("xizehao",ddanxizList.get(j).getXizehao());
                    map.put("num",ddanxizList.get(j).getGnum());
                    map.put("price",ddanxizList.get(j).getGprice());
                    map.put("total",ddanxizList.get(j).getTotal());
                    map.put("status",ddanxizList.get(j).getStatus());
                    mapList.add(map);
                }
            }
            mdata.add(mapList);
            pdata.add(pmap);
        }

    }

}

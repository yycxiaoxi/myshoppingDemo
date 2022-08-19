package com.example.xiaomaibu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.example.xiaomaibu.Bean.Pingjia;
import com.example.xiaomaibu.Bean.Refund;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.PingjiaDao;
import com.example.xiaomaibu.Dao.RefundDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.GoodsInfoActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class daifahuoActivity extends AppCompatActivity {
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private List<Order> orderList;
    private List<Ddanxiz> ddanxizList;
    private List<Goods> goodsList;
    private ExpandableListView expandableListView;
    private String uid="123456";
    private Context mContext;
    private daifahuoAdapter daifahuoAdapter;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daifahuo);
        expandableListView=findViewById(R.id.expandableListView);
        mContext=daifahuoActivity.this;
        progressBar = findViewById(R.id.progress_circular);
        TemploginDao temploginDao=new TemploginDao();
        uid=temploginDao.find(daifahuoActivity.this);
        if(uid==null){
            Intent intent=new Intent(daifahuoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        Intent intent=getIntent();
        int status=Integer.parseInt(intent.getStringExtra("status"));
        //找顾客已支付的订单
        String url=null;
        switch (status){
            case 1:url="/orderStatus/1";break;
            case 2:url="/orderStatus/2";break;
            case 4:url="/orderStatus/3";break;
        }
        try {
            progressBar.setVisibility(View.VISIBLE);
            OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order" + url, "", mContext, new Callback() {
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
                        daifahuoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (orderList.size()!=0){

                                    //初始化数据
                                    initData();
                                    daifahuoAdapter=new daifahuoAdapter(mContext,pdata,mdata);
                                    expandableListView.setAdapter(daifahuoAdapter);
                                    for (int i=0;i<pdata.size();i++){
                                        expandableListView.expandGroup(i);
                                    }

                                    expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                        @Override
                                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                            String gid=(String) mdata.get(groupPosition).get(childPosition).get("gid");
                                            Intent intent = new Intent(daifahuoActivity.this, GoodsInfoActivity.class);
                                            intent.putExtra("gid",gid);
                                            startActivity(intent);
                                            return true;
                                        }
                                    });

                                    daifahuoAdapter.setshouhuoBtn(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int posation=(int)v.getTag();
                                            String orderno=(String) pdata.get(posation).get("orderno");
                                            try {
                                                shouHuo(posation,orderno);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                    daifahuoAdapter.setGrouponLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            final int posation=(int)v.getTag();
                                            final String orderno=(String) pdata.get(posation).get("orderno");
                                            //Toast.makeText(daifahuoActivity.this,orderno,Toast.LENGTH_SHORT).show();
                                            AlertDialog.Builder builder=new AlertDialog.Builder(daifahuoActivity.this);
                                            final EditText editText=new EditText(daifahuoActivity.this);
                                            builder.setView(editText);
                                            builder.setMessage("请输入你的退货理由");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String info = editText.getText().toString();
                                                    try {
                                                        refundaction(info,orderno,posation);//退款动作
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                            builder.setNegativeButton("取消",null);
                                            builder.create().show();
                                            return true;
                                        }
                                    });

                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * 申请退款
     * @param info
     * @param orderno
     * @param posation
     * @throws IOException
     */
    private void refundaction(String info, String orderno, final int posation) throws IOException {
        Refund refund=new Refund();
        refund.setInfo(info);
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = tempDate.format(new java.util.Date());
        refund.setTime(datetime);
        refund.setOrderno(orderno);
        refund.setStatus("0");
        refund.setRefundno(UUID.randomUUID().toString());
        //refundDao.insert(daifahuoActivity.this,refund);
        String json = JSON.toJSONString(refund);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/refund/insert", json, mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(daifahuoActivity.this,"网络错误",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    mdata.remove(posation);
                    pdata.remove(posation);
                    orderList.remove(posation);
                        daifahuoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(daifahuoActivity.this,"申请成功",Toast.LENGTH_SHORT).show();
                                daifahuoAdapter.notifyDataSetChanged();
                            }
                        });
                }
            }
        });
    }
    private void initData(){
        mdata=new ArrayList<>();
        pdata=new ArrayList<>();
        for(int i=0;i<orderList.size();i++){
            List<Map<String,Object>> mapList=new ArrayList<>();
            Map pmap=new HashMap();
            pmap.put("status",orderList.get(i).getStatus());
            pmap.put("orderno",orderList.get(i).getOrderno());
            pmap.put("createtime",orderList.get(i).getCreatetime());
            pmap.put("paytime",orderList.get(i).getPaytime());
            pmap.put("sendtime",orderList.get(i).getSendtime());
            pmap.put("artime",orderList.get(i).getArtime());
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
                    map.put("num",ddanxizList.get(j).getGnum());
                    map.put("price",ddanxizList.get(j).getGprice());
                    map.put("total",ddanxizList.get(j).getTotal());
                    mapList.add(map);
                }
            }
            mdata.add(mapList);
            pdata.add(pmap);
        }

    }

    /**
     * 收货按钮service
     * @param i
     * @param order_no
     * @throws IOException
     */
    private void shouHuo(final int i,String order_no) throws IOException {
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/shouHuo/"+order_no, "", mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    mdata.remove(i);
                    pdata.remove(i);
                    daifahuoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            daifahuoAdapter.notifyDataSetChanged();
                        }
                    });
                }


            }
        });
    }
}

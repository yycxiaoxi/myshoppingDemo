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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.RefundAdapter;
import com.example.xiaomaibu.Adapter.daifahuoAdapter;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Bean.Refund;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
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
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RefundActivity extends AppCompatActivity {
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private List<Order> orderList;
    private List<Ddanxiz> ddanxizList;
    private List<Goods> goodsList;
    private ExpandableListView expandableListView;
    private String uid="123456";
    private Context context;
    private RefundAdapter refundAdapter;
    private List<Refund> refundList;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daifahuo);
        context = RefundActivity.this;
        expandableListView = findViewById(R.id.expandableListView);
        progressBar = findViewById(R.id.progress_circular);
        TemploginDao temploginDao = new TemploginDao();
        uid = temploginDao.find(RefundActivity.this);
        if (uid == null) {
            Intent intent = new Intent(RefundActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        //找顾客已支付的订单
        try {
            getRefund();
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

            Refund refund = refundList.get(i);
            //Log.e("nihao",refund.toString());
            pmap.put("time",refund.getTime());
            pmap.put("status",refund.getStatus());
            pmap.put("info",refund.getInfo());
            pmap.put("orderno",orderList.get(i).getOrderno());
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
     *   退款重新申请
     */
    private void refundaction(String info, String orderno, final int posation) throws IOException {
        Refund refund=new Refund();
        refund.setInfo(info);
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = tempDate.format(new java.util.Date());
        refund.setTime(datetime);
        refund.setOrderno(orderno);
        refund.setStatus("0");

        String json = JSON.toJSONString(refund);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/refund/update", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    mdata.remove(posation);
                    pdata.remove(posation);
                    orderList.remove(posation);
                    RefundActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RefundActivity.this,"申请成功",Toast.LENGTH_SHORT).show();
                            refundAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    /**
     * 取消退款
     */
    private void cancelRefund(String orderno,final int posation) throws IOException {
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/refund/delete/" + orderno, "", context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body =response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    mdata.remove(posation);
                    pdata.remove(posation);
                    orderList.remove(posation);

                    RefundActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            refundAdapter.notifyDataSetChanged();
                            Toast.makeText(RefundActivity.this,"取消退款成功",Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }
        });
    }

    /**
     * 查询退款记录
     */
    private void  getRefund() throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtils.httpGet(OkhttpUtils.IP + "/order/getRefund", context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status  = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    JSONObject jsonObject = (JSONObject) JSON.parseObject(body).get("data");
                    JSONArray ddanxizListString   = jsonObject.getJSONArray("ddanxizList");
                    JSONArray goodsListString   = jsonObject.getJSONArray("goodsList");
                    JSONArray orderListString   = jsonObject.getJSONArray("orderList");
                    JSONArray refundString   = jsonObject.getJSONArray("refundList");
                    ddanxizList = JSON.parseObject(ddanxizListString.toJSONString(),new TypeReference<List<Ddanxiz>>(){});
                    goodsList =JSON.parseObject(goodsListString.toJSONString(),new TypeReference<List<Goods>>(){});
                    orderList =JSON.parseObject(orderListString.toJSONString(),new TypeReference<List<Order>>(){});
                    refundList =JSON.parseObject(refundString.toJSONString(),new TypeReference<List<Refund>>(){});

                    if (orderList.size() != 0) {
                        //初始化数据
                        initData();
                        RefundActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refundAdapter = new RefundAdapter(context, pdata, mdata);
                                expandableListView.setAdapter(refundAdapter);
                                for (int i=0;i<pdata.size();i++){
                                    expandableListView.expandGroup(i);
                                }

                                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                    @Override
                                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                        String gid = (String) mdata.get(groupPosition).get(childPosition).get("gid");
                                        Intent intent = new Intent(RefundActivity.this, GoodsInfoActivity.class);
                                        intent.putExtra("gid", gid);
                                        startActivity(intent);
                                        return true;
                                    }
                                });

                                refundAdapter.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        final int posation=(int)v.getTag();
                                        String status=pdata.get(posation).get("status").toString();
                                        if(status.equals("2")){
                                            AlertDialog.Builder builder=new AlertDialog.Builder(RefundActivity.this);
                                            builder.setTitle("申请退款");
                                            builder.setMessage("你确定要重新申请退款吗");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    AlertDialog.Builder builder=new AlertDialog.Builder(RefundActivity.this);
                                                    builder.setTitle("请再次输入你的退款理由");
                                                    final EditText editText=new EditText(RefundActivity.this);
                                                    builder.setView(editText);
                                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String orderno=pdata.get(posation).get("orderno").toString();
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

                                                }
                                            });
                                            builder.setNegativeButton("取消退款", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String orderno=pdata.get(posation).get("orderno").toString();
                                                    try {
                                                        cancelRefund(orderno,posation);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                            builder.create().show();
                                        }
                                        if(status.equals("0")){
                                            AlertDialog.Builder builder=new AlertDialog.Builder(RefundActivity.this);
                                            builder.setTitle("取消退款");
                                            builder.setMessage("你确定取消退款吗");
                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String orderno=pdata.get(posation).get("orderno").toString();
                                                    try {
                                                       // progressBar.setVisibility(View.VISIBLE);
                                                        cancelRefund(orderno,posation);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                            builder.setNegativeButton("否",null);
                                            builder.create().show();
                                        }
                                        return true;
                                    }
                                });
                                progressBar.setVisibility(View.GONE);

                            }


                        });

                    }else {
                        RefundActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RefundActivity.this,"没有退款记录",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        });
    }
}

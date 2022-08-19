package com.example.xiaomaibu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.AdminSetDaohuoAdapter;
import com.example.xiaomaibu.Adapter.AdminSetRefundAdapter;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Bean.Refund;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.RefundDao;
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

public class adminFourthFragment extends Fragment {

    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;

    private List<Order> orderList;
    private List<Ddanxiz> ddanxizList;
    private List<Goods> goodsList;
    private ExpandableListView expandableListView;
    private String uid="123456";
    private Context context;
    private List<Refund> refundList;
    private ProgressBar progressBar;
    private AdminSetRefundAdapter adminSetRefundAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        View root=inflater.inflate(R.layout.fragment_admin_fourth, container, false);
        expandableListView=root.findViewById(R.id.expandableListView);
        context=getContext();
        progressBar=root.findViewById(R.id.progress_circular);


        try {
            progressBar.setVisibility(View.VISIBLE);
            getRefund();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }


    /**
     * 拉去用户退款信息
     */
    private void getRefund() throws IOException {
        OkhttpUtils.httpGet(OkhttpUtils.IP + "/order/refundall", context, new Callback() {
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
                    if (orderList.size()!=0){
                        //初始化数据
                        initData();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adminSetRefundAdapter=new AdminSetRefundAdapter(context,pdata,mdata);
                                expandableListView.setAdapter(adminSetRefundAdapter);
                                expandableListView.expandGroup(0);
                                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                    @Override
                                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                        String gid=(String) mdata.get(groupPosition).get(childPosition).get("gid");
                                        Intent intent = new Intent(context, GoodsInfoActivity.class);
                                        intent.putExtra("gid",gid);
                                        startActivity(intent);
                                        return true;
                                    }
                                });
                                adminSetRefundAdapter.setRefundListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        int pasation=(int)v.getTag();
                                        String orderno = (String) pdata.get(pasation).get("orderno");
                                        try {
                                            refund(orderno,pasation);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                adminSetRefundAdapter.setNorefundListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final int pasation=(int)v.getTag();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                        builder.setTitle("请输入不能退款的理由");
                                        final EditText editText=new EditText(context);
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String info=editText.getText().toString();
                                                SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String datetime = tempDate.format(new java.util.Date());
                                                Refund refund=refundList.get(pasation);
                                                refund.setStatus("2");//不能退款是2
                                                refund.setTime(datetime);
                                                refund.setInfo(info);
                                                try {
                                                    refundUpdate(refund,pasation);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });
                                        builder.setNegativeButton("取消",null);
                                        builder.setView(editText);
                                        builder.create().show();

                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context,"没有退款信息",Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        });

    }

    /**
     * 不给退款
     * @param refund
     * @param posation
     * @throws IOException
     */
    private  void refundUpdate(Refund refund,final int posation) throws IOException {
        String json = JSON.toJSONString(refund);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/refund/update", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status  = OkhttpUtils.getCode(body);
                if ("200".equals(status)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdata.remove(posation);
                            mdata.remove(posation);
                            refundList.remove(posation);
                            orderList.remove(posation);
                            adminSetRefundAdapter.notifyDataSetChanged();
                            Toast.makeText(context,"操作成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refund(String orderno,final int posation) throws IOException {
        progressBar.setVisibility(View.VISIBLE);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/alipay/refund/"+orderno, "", context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                JSONObject jsonObject = JSON.parseObject(body);
                JSONObject jsonObject1 = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code = jsonObject1.getString("code");
                if ("10000".equals(code)){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pdata.remove(posation);
                            mdata.remove(posation);
                            refundList.remove(posation);
                            orderList.remove(posation);
                            adminSetRefundAdapter.notifyDataSetChanged();
                            Toast.makeText(context,"退款成功",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"退款失败",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
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
            Refund refund =refundList.get(i);
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




}

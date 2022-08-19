package com.example.xiaomaibu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.AdminDaifahuoAdapter;
import com.example.xiaomaibu.Adapter.AdminSetDaohuoAdapter;
import com.example.xiaomaibu.Adapter.daifahuoAdapter;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.GoodsInfoActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class AdminThirdFragment extends Fragment {
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private OrderDao orderDao;
    private DdanxizDao ddanxizDao;
    private GoodsDao goodsDao;
    private List<Order> orderList;
    private List<Ddanxiz> ddanxizList;
    private List<Goods> goodsList;
    private ExpandableListView expandableListView;
    private String uid="123456";
    private Context context;
    private AdminSetDaohuoAdapter adminSetDaohuoAdapter;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment
        View root=inflater.inflate(R.layout.fragment_admin_third, container, false);
        expandableListView=root.findViewById(R.id.expandableListView);
        context=getContext();
        progressDialog = new ProgressDialog(getActivity());
        try {
            daishouhuoAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }
    private void initData(){
        mdata=new ArrayList<>();
        pdata=new ArrayList<>();
        for(int i=0;i<orderList.size();i++){
            List<Map<String,Object>> mapList=new ArrayList<>();
            Map pmap=new HashMap();
            pmap.put("orderno",orderList.get(i).getOrderno());
            pmap.put("sendtime",orderList.get(i).getSendtime());
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
     * 拉去已经发货信息
     * @throws IOException
     */
    private void daishouhuoAll() throws IOException {

        OkhttpUtils.httpGet(OkhttpUtils.IP + "/order/admin/daishouhuo", context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body= response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    JSONObject jsonObject = (JSONObject) JSON.parseObject(body).get("data");
                    Log.e("body",jsonObject.toJSONString());
                    JSONArray ddanxizListString   = jsonObject.getJSONArray("ddanxizList");
                    JSONArray goodsListString   = jsonObject.getJSONArray("goodsList");
                    JSONArray orderListString   = jsonObject.getJSONArray("orderList");
                    ddanxizList = JSON.parseObject(ddanxizListString.toJSONString(),new TypeReference<List<Ddanxiz>>(){});
                    goodsList =JSON.parseObject(goodsListString.toJSONString(),new TypeReference<List<Goods>>(){});
                    orderList =JSON.parseObject(orderListString.toJSONString(),new TypeReference<List<Order>>(){});
                    initData();
                    if (pdata.size()!=0){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adminSetDaohuoAdapter=new AdminSetDaohuoAdapter(context,pdata,mdata);
                                expandableListView.setAdapter(adminSetDaohuoAdapter);
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
                                adminSetDaohuoAdapter.setClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int pasation=(int)v.getTag();
                                        Order order= orderList.get(pasation);
                                        try {
                                            progressDialog.setCancelable(false);
                                            progressDialog.show();
                                            orderUpdate(order,pasation);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }
                        });
                    }

                }
            }
        });
    }
    private void orderUpdate(Order order, final int posation) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String artime  =  df.format(new Date());// new Date()为获取当前系统时间
        order.setArtime(artime);
        order.setStatus(3);
        String json = JSON.toJSONString(order);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/admin/update", json, context, new Callback() {
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (pdata.size()>=0){
                                adminSetDaohuoAdapter.notifyDataSetChanged();
                                Toast.makeText(context,"发货成功",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }

                        }
                    });
                }

            }
        });
    }

}

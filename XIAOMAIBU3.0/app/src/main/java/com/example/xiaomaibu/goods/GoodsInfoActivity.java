package com.example.xiaomaibu.goods;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Adapter.GoodsInfoAdapter;
import com.example.xiaomaibu.Adapter.Myadapter;
import com.example.xiaomaibu.Bean.Cart;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Bean.Pingjia;
import com.example.xiaomaibu.Dao.CartDao;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.PingjiaDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.nopayActivity;

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

public class GoodsInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView name;
    private TextView price;
    private Button payBtn;
    private Button addcartBtn;
    private Button onAddNum;
    private Button onSubNum;
    private TextView numtv;
    private Goods goods;
    private ListView listView;
    private String uid;
    private Context mContext;
    private GoodsInfoAdapter goodsInfoAdapter;
    private List<Pingjia> pingjiaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);
        imageView = findViewById(R.id.goods_info_img);
        name = findViewById(R.id.goods_info_name);
        price = findViewById(R.id.goods_info_price);
        payBtn = findViewById(R.id.payBtn);
        addcartBtn = findViewById(R.id.addcartBtn);
        onAddNum = findViewById(R.id.onAddNum);
        onSubNum = findViewById(R.id.onSubNum);
        numtv = findViewById(R.id.num);
        listView = findViewById(R.id.goods_info_list_pingjia);
        mContext = GoodsInfoActivity.this;
        Intent intent = getIntent();
        //得到点击的商品id
        String gid = intent.getStringExtra("gid");
        /**
         * 获取商品
         */
        try {
            getGoods_Gid(gid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * 获取评价
         */
        try {
            getPingjia(gid);

        } catch (IOException e) {
            e.printStackTrace();
        }

        setListViewHeightBasedOnChildren(listView);
        onAddNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numtv.getText().toString());
                num++;
                numtv.setText(num + "");
            }
        });
        onSubNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(numtv.getText().toString());
                if (num > 1) {
                    num--;
                } else {
                    Toast.makeText(GoodsInfoActivity.this, "数量不能为零蛋哦", Toast.LENGTH_SHORT).show();
                }
                numtv.setText(num + "");
            }
        });
        addcartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemploginDao temploginDao = new TemploginDao();
                uid = temploginDao.find(GoodsInfoActivity.this);
                if (uid == null) {
                    Intent intent = new Intent(GoodsInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    addcart();
                }
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemploginDao temploginDao = new TemploginDao();
                uid = temploginDao.find(GoodsInfoActivity.this);
                if (uid == null) {
                    Intent intent = new Intent(GoodsInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    try {
                        buy();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }


    //添加购物车
    private void addcart() {
        int num = Integer.parseInt(numtv.getText().toString());
        String gid = goods.getGid();
        CartDao cartDao = new CartDao();
        Cart cart = cartDao.find(GoodsInfoActivity.this, gid, uid);
        if (cart == null) {
            cart = new Cart();
            cart.setUid(uid);
            cart.setNum(num);
            cart.setGid(gid);
            boolean flag = cartDao.insert(GoodsInfoActivity.this, cart);
            if (flag) {
                Toast.makeText(GoodsInfoActivity.this.getApplicationContext(), "添加购物车成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GoodsInfoActivity.this.getApplicationContext(), "添加购物车失败", Toast.LENGTH_SHORT).show();

            }
        } else {
            cart.setNum(cart.getNum() + num);
            boolean flag = cartDao.update(GoodsInfoActivity.this, cart);
            if (flag) {
                Toast.makeText(GoodsInfoActivity.this.getApplicationContext(), "添加购物车成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GoodsInfoActivity.this.getApplicationContext(), "添加购物车失败", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //直接购买
    private void buy() throws IOException {
        sendOrder(CreateOrder());

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight() + 20;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 从后台取点击商品的数据
     */
    private void getGoods_Gid(String gid) throws IOException {

        OkhttpUtils.httpGet(OkhttpUtils.IP + "/goods/find/" + gid, mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String body = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    String status = jsonObject.get("code").toString();
                    if (status.equals("200")) {
                        String jsonArray = jsonObject.getString("data");
                        Log.d("jsonArray", jsonArray);
                        goods = JSON.parseObject(jsonArray, Goods.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText(goods.getName());
                                price.setText(goods.getPrice() + "");
                                Glide.with(mContext).load(OkhttpUtils.imgIP + goods.getImg())
                                        .into(imageView);
                            }
                        });
                    }
                }
            }
        });

    }

    private void getPingjia(String gid) throws IOException {
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/eva/find/gid/" + gid, "", mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if ("200".equals(OkhttpUtils.getCode(body))) {
                    pingjiaList = JSON.parseObject(OkhttpUtils.getData(body).toJSONString()
                            , new TypeReference<List<Pingjia>>() {
                            });
                    GoodsInfoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodsInfoAdapter = new GoodsInfoAdapter(GoodsInfoActivity.this, pingjiaList);
                            listView.setAdapter(goodsInfoAdapter);
                            goodsInfoAdapter.notifyDataSetChanged();
                        }
                    });

                }


            }
        });
    }

    private void sendOrder(Map<String, Object> orderMap) throws IOException {
        String json = JSON.toJSONString(orderMap);
        Log.e("json", json);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/insert", json, mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    String code = OkhttpUtils.getCode(body);
                    if ("200".equals(code)) {
                        GoodsInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent1 = new Intent(GoodsInfoActivity.this, nopayActivity.class);
                                Toast.makeText(GoodsInfoActivity.this, "提交订单成功", Toast.LENGTH_SHORT).show();
                                startActivity(intent1);
                            }
                        });
                    }

                }

            }
        });
    }

    /**
     * 创建订单和订单细则
     *
     * @return
     */
    private Map<String, Object> CreateOrder() {
        SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = tempDate.format(new java.util.Date());
        Order order = new Order();
        String orderno = UUID.randomUUID().toString();
        order.setOrderno(orderno);
        order.setId(uid);
        order.setStatus(0);
        order.setCreatetime(datetime);
        order.setAgrtime("");
        order.setArtime("");
        List<Ddanxiz> ddanxizList = new ArrayList<>();
        float orderprice = 0;
        Ddanxiz ddanxiz = new Ddanxiz();
        String gid = goods.getGid();
        float price = goods.getPrice();
        int num = Integer.parseInt(numtv.getText().toString());
        float toprice = num * price;
        orderprice += toprice;
        ddanxiz.setGid(gid);
        ddanxiz.setGnum(num);
        ddanxiz.setGprice(price);
        ddanxiz.setOrderno(orderno);
        ddanxiz.setStatus("0");
        ddanxiz.setTotal(toprice);
        ddanxiz.setXizehao(UUID.randomUUID().toString());
        ddanxizList.add(ddanxiz);
        order.setTotal(orderprice);
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("order", order);
        orderMap.put("ddanxiz", ddanxizList);
        return orderMap;

    }
}







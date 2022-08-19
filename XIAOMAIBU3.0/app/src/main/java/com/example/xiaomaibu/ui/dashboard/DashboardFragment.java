package com.example.xiaomaibu.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.example.xiaomaibu.Adapter.CartAdapter;
import com.example.xiaomaibu.Bean.Cart;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Dao.CartDao;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;

import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Service.GoodsService;
import com.example.xiaomaibu.Service.Impl.GoodsServiceImpl;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.GoodsInfoActivity;
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

public class DashboardFragment extends Fragment {
    private ListView listView;
    private List<Cart> cartList;
    private List<Map<String, Object>> mapList;
    private Context context;
    private CartAdapter cartAdapter;
    private TextView tv_total_price;
    private CheckBox all_chekbox;
    private Button payBtn;
    private String uid="123456";
    private GoodsService goodsService;
    private Handler handler;
    private CartDao cartDao;
    private List<Goods> goodsList;
    private List<Integer> removeList;
    List<String>  gidList;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        context = getContext();
        TemploginDao temploginDao = new TemploginDao();
        goodsService = new GoodsServiceImpl();
        uid = temploginDao.find(context);
        if (uid == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tv_total_price = root.findViewById(R.id.tv_total_price);
        all_chekbox = root.findViewById(R.id.all_chekbox);
        payBtn = root.findViewById(R.id.payBtn);
        listView = root.findViewById(R.id.listView);

        cartDao = new CartDao();
        cartList = cartDao.findall(context, uid);//获取用户购物车列表
        gidList=getGid();//获取商品id列表
        try {
            getGoodsList2();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //getGoodsList(gidList);//从后台拉去数据
        mapList = new ArrayList<>();



        return root;
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
            totalHeight += listItem.getMeasuredHeight() + 100;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 后台返回商品信息后初始化购物车map
     * @return
     */
    private List<Map<String, Object>> initData() {
        for (Goods goods: goodsList) {
            Cart cart= cartDao.find(context,goods.getGid(),uid);
            Map<String, Object> map = new HashMap();
            map.put("gid", goods.getGid());
            map.put("name", goods.getName());
            map.put("aid", goods.getGid());
            map.put("cost", goods.getCost());
            map.put("price", goods.getPrice());
            map.put("img", goods.getImg());
            map.put("sort", goods.getSort());
            map.put("cartnum", cart.getNum());
            map.put("checked", false);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 获取商品id
     * @return
     */
    private List<String> getGid(){
        List<String> gidList= new ArrayList<>();
        for (Cart cart : cartList) {
            gidList.add(cart.getGid());
        }
        return gidList;
    }


    /**
     * 求当前的商品价格
     * @return
     */
    private float sun() {
        float sum = 0;
        int num;
        float price;
        for (int i = 0; i < mapList.size(); i++) {

            num = (int) mapList.get(i).get("cartnum");
            price = (float) mapList.get(i).get("price");
            sum += num * price;
        }
        return sum;
    }
    private float sum(){
        float sum = 0;
        int num;
        float price;
        boolean flag ;
        for (int i = 0; i < mapList.size(); i++) {
            flag = (boolean) mapList.get(i).get("checked");
            if (flag){
                num = (int) mapList.get(i).get("cartnum");
                price = (float) mapList.get(i).get("price");
                sum += num * price;
            }

        }
        return sum;

    }

    /**
     * 创建订单和订单细则
     * @return
     */
    private Map<String,Object> CreateOrder() {
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

        removeList = new ArrayList<>();//记录要付款的下标，下单成功后从购物车删除
        List<Ddanxiz> ddanxizList =new ArrayList<>();
        CartDao cartDao = new CartDao();
        float orderprice = 0;
        for (int i = 0; i < mapList.size(); i++) {
            if (mapList.get(i).get("checked").equals(true)) {
                Ddanxiz ddanxiz = new Ddanxiz();
                String gid = (String) mapList.get(i).get("gid");
                float price = (float) mapList.get(i).get("price");
                int num = (int) mapList.get(i).get("cartnum");
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
                //cartDao.delete(context, gid, uid);
                removeList.add(i);

            }
        }
        order.setTotal(orderprice);
        Map<String,Object> orderMap=new HashMap<>();
        orderMap.put("order",order);
        orderMap.put("ddanxiz",ddanxizList);
        return orderMap;

    }

    /**
     * 从后台获取商品信息
     */

    private void getGoodsList2() throws IOException {
        String json= JSON.toJSONString(gidList);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/goods/find/cart", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                JSONArray jsonArray=OkhttpUtils.getData(body);
                goodsList = JSON.parseObject(jsonArray.toJSONString(),
                        new TypeReference<List<Goods>>(){});
                initData();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mapList.size() != 0) {
                            cartAdapter = new CartAdapter(context, mapList);
                            listView.setAdapter(cartAdapter);
                            setListViewHeightBasedOnChildren(listView);
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, long id) {
                                    final Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("不要我了吗QAQ?");
                                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            boolean f=true,f2=true;
                                            float dprice=0;
                                            int dcartnum=0;
                                            boolean flag = cartDao.delete(context, map.get("gid").toString(), uid);
                                            if (flag) {
                                                Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();


                                                if (position<mapList.size()-1){
                                                    f= (boolean) mapList.get(position+1).get("checked");
                                                    f2=(boolean) mapList.get(position).get("checked");
                                                    dprice= (Float) mapList.get(position+1).get("price");
                                                    dcartnum= (int) mapList.get(position+1).get("cartnum");
                                                }
                                                mapList.remove(position);
                                                if (all_chekbox.isChecked()){
                                                    all_chekbox.setChecked(false);
                                                    tv_total_price.setText(sun() + "");

                                                }else {
                                                    if (mapList.size()!=0){
                                                        if (position<mapList.size() && position>0 || position==0){//第一个

                                                            if (!f && f2){
                                                                //Log.e("sum1",sum()+dprice*dcartnum+"");
                                                                tv_total_price.setText(sum()+dprice*dcartnum+"");
                                                            }else {
                                                              //  Log.e("sum2",sum()+"");
                                                                tv_total_price.setText(sum()+"");
                                                            }
                                                        }else {//最后一个
                                                            //Log.e("sum3",sum()+"");
                                                            tv_total_price.setText(sum()+"");
                                                        }

                                                    }else {
                                                        tv_total_price.setText("0");
                                                    }
                                                }

                                                cartAdapter.notifyDataSetChanged();

                                            } else {
                                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("取消", null);
                                    builder.create().show();
                                    return true;
                                }
                            });

                            //计算总共价格

                            //多选框回调方法
                            cartAdapter.setCheckBox(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton v, boolean isChecked) {

                                    int posation = (int) v.getTag();
                                    float toprice = Float.parseFloat(tv_total_price.getText().toString());
                                    if (v.isChecked()) {
                                        float num = Float.parseFloat(mapList.get(posation).get("cartnum").toString());
                                        float price = Float.parseFloat(mapList.get(posation).get("price").toString());
                                        toprice += num * price;
                                        tv_total_price.setText(toprice + "");
                                        mapList.get(posation).put("checked", true);


                                    }
                                    if (!v.isChecked()) {
                                        float num = Float.parseFloat(mapList.get(posation).get("cartnum").toString());
                                        float price = Float.parseFloat(mapList.get(posation).get("price").toString());
                                        toprice -= num * price;
                                        tv_total_price.setText(toprice + "");
                                        mapList.get(posation).put("checked", false);

                                    }

                                    cartAdapter.notifyDataSetChanged();
                                }
                            });
                            //全选按钮事件
                            all_chekbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        for (int i = 0; i < mapList.size(); i++) {
                                            mapList.get(i).put("checked", true);
                                        }
                                    }
                                    if (!isChecked) {
                                        for (int i = 0; i < mapList.size(); i++) {
                                            mapList.get(i).put("checked", false);
                                        }

                                    }

                                    cartAdapter.notifyDataSetChanged();

                                }
                            });
                            cartAdapter.setOnAddNum(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int posasion = (int) v.getTag();
                                    int num = (int) mapList.get(posasion).get("cartnum");
                                    mapList.get(posasion).put("cartnum", num + 1);
                                    cartList.get(posasion).setNum(num + 1);
                                    cartDao.update(context, cartList.get(posasion));
                                    boolean checked = (boolean) mapList.get(posasion).get("checked");
                                    if (checked) {//如果被勾选则更改总价格
                                        float price = (float) mapList.get(posasion).get("price");
                                        float toprice = Float.parseFloat(tv_total_price.getText().toString());
                                        toprice += price;
                                        tv_total_price.setText("" + toprice);
                                    }
                                    cartAdapter.notifyDataSetChanged();
                                }
                            });

                            cartAdapter.setOnSubNum(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int posasion = (int) v.getTag();
                                    int num = (int) mapList.get(posasion).get("cartnum");
                                    if (num > 1) {
                                        mapList.get(posasion).put("cartnum", num - 1);//更新maplist
                                        cartList.get(posasion).setNum(num - 1);
                                        cartDao.update(context, cartList.get(posasion));//更新数据库
                                        boolean checked = (boolean) mapList.get(posasion).get("checked");
                                        if (checked) {//如果被勾选则更改总价格
                                            float price = (float) mapList.get(posasion).get("price");
                                            float toprice = Float.parseFloat(tv_total_price.getText().toString());
                                            toprice -= price;
                                            tv_total_price.setText("" + toprice);
                                        }
                                        cartAdapter.notifyDataSetChanged();//通知更改
                                    } else {
                                        Toast.makeText(context, "数量不能小于1", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                            payBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    float toprice = Float.parseFloat(tv_total_price.getText().toString());
                                    if (toprice == 0) {
                                        Toast.makeText(context, "请选择你喜爱的商品哦", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Map<String,Object> orderMap=CreateOrder();
                                        try {
                                            sendOrder(orderMap);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                            //列表子项被点击触发事件
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    String gid = (String) mapList.get(i).get("gid");
                                    Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                                    intent.putExtra("gid", gid);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });

            }
        });

    }

    /**
     * 把订单发送到后台
     */
    private void sendOrder(Map<String,Object> orderMap) throws IOException {
        String json = JSON.toJSONString(orderMap);
        Log.e("json",json);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/insert", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    String body = response.body().string();
                    String code=OkhttpUtils.getCode(body);
                    if ("200".equals(code)){
                        setRemoveList();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                set_tv_total_price();
                                cartAdapter.notifyDataSetChanged();
                                Toast.makeText(context, "提交订单成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), nopayActivity.class);
                                startActivity(intent);
                                getActivity().onBackPressed();
                            }
                        });
                    }

                }

            }
        });
    }
    private  void setRemoveList(){
        int sum = 0;
        CartDao cartDao=new CartDao();
        for (int i= 0 ;i<removeList.size();i++){
            String gid =(String) mapList.get(removeList.get(i)-sum).get("gid");
            cartDao.delete(context,gid,uid);
            mapList.remove(removeList.get(i)-sum);
            sum++;
        }
    }
    private void set_tv_total_price(){
        int total_price=0;
        for (int i = 0;i<mapList.size();i++){
            float price = (float) mapList.get(i).get("price");
            int num = (int) mapList.get(i).get("cartnum");
            total_price += price* num;
        }

        tv_total_price.setText(total_price+"");
    }

}
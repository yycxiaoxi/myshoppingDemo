package com.example.xiaomaibu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.xiaomaibu.Adapter.NoPayAdapter;
import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Dao.CartDao;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.Util.AlipayUtils.AuthResult;
import com.example.xiaomaibu.Util.AlipayUtils.PayResult;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.GoodsInfoActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.xiaomaibu.Alipay.alipayConfig.APPID;
import static com.example.xiaomaibu.Alipay.alipayConfig.RSA2_PRIVATE;
import static com.example.xiaomaibu.Alipay.alipayConfig.RSA_PRIVATE;

public class  nopayActivity extends AppCompatActivity {
    private String uid="123456";
    private List<Order> orderList;
    private List<Ddanxiz> ddanxizList;
    private List<Goods> goodsList;
    private List<List<Map<String,Object>>>  mdata;
    private List<Map<String,Object>> pdata;
    private CheckBox all_chekbox;
    private ExpandableListView expandableListView;
    private TextView tv_total_price;
    private Button payBtn;
    private Context mContext;
    private NoPayAdapter noPayAdapter;
    private List<Integer> removeList;
    private ProgressBar progressBar;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                     */
                    String resultInfo = payResult.getResult();// ?????????????????????????????????
                    String resultStatus = payResult.getResultStatus();
                    // ??????resultStatus ???9000?????????????????????
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // ??????????????????????????????????????????????????????????????????????????????
                        showAlert(nopayActivity.this, "????????????" + payResult);
                        setRemoveList();
                        tv_total_price.setText("0");
                        noPayAdapter.notifyDataSetChanged();

                    } else {
                        // ???????????????????????????????????????????????????????????????????????????
                        showAlert(nopayActivity.this, "????????????" + payResult);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // ??????resultStatus ??????9000??????result_code
                    // ??????200?????????????????????????????????????????????????????????????????????????????????
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // ??????alipay_open_id???????????????????????????extern_token ???value
                        // ??????????????????????????????????????????
                        showAlert(nopayActivity.this, "????????????" + authResult);
                    } else {
                        // ?????????????????????????????????
                        showAlert(nopayActivity.this, "????????????" + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);//???????????????????????????
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nopay);
        mContext=nopayActivity.this;
        progressBar = findViewById(R.id.progress_horizontal);
        TemploginDao temploginDao=new TemploginDao();
        uid=temploginDao.find(nopayActivity.this);
        if(uid==null){
            Intent intent=new Intent(nopayActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        expandableListView=findViewById(R.id.expandableListView);
        tv_total_price=findViewById(R.id.tv_total_price);
        all_chekbox=findViewById(R.id.all_chekbox);
        payBtn=findViewById(R.id.payBtn);
        try {
            progressBar.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                progressBar.setMin(0);
            }
            getInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //???????????????
    private void initData(){
        mdata=new ArrayList<>();
        pdata=new ArrayList<>();
        for(int i=0;i<orderList.size();i++){
            List<Map<String,Object>> mapList=new ArrayList<>();
            Map pmap=new HashMap();
            pmap.put("orderno",orderList.get(i).getOrderno());
            pmap.put("createtime",orderList.get(i).getCreatetime());
            pmap.put("total",orderList.get(i).getTotal());
            pmap.put("checked",false);
            for(int j=0;j<ddanxizList.size();j++){
                if(ddanxizList.get(j).getOrderno().equals(orderList.get(i).getOrderno())){//???????????????????????????
                    Map<String,Object> map=new HashMap<>();
                    map.put("xizehao",ddanxizList.get(i).getGid());
                    for (int k=0;k<goodsList.size();k++){//?????????????????????
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

    //??????
    private float sum(){
        float sum=0;
        for (int i=0;i<pdata.size();i++){
            float price =(float)pdata.get(i).get("total");
            sum+=price;
        }
        return sum;
    }
    //??????
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)


    private  void pay() throws IOException {
        removeList = new ArrayList<>();
        Float toprice=Float.parseFloat(tv_total_price.getText().toString());
        if (toprice>0){
            List<String> order_noList = new ArrayList<>();
            for(int i=0;i<pdata.size();i++){
                if(pdata.get(i).get("checked").equals(true)){
                    String orderno =(String) pdata.get(i).get("orderno");
                    order_noList.add(orderno);
                    removeList.add(i);//????????????????????????

                }
            }
            /*
             *
             *
             * orderInfo ?????????????????????????????????
             */
            String json = JSON.toJSONString(order_noList);
            OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/alipay/pay", json, mContext, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String body =response.body().string();
                    String status = OkhttpUtils.getCode(body);
                    if ("200".equals(status)){
                        JSONObject jsonObject =JSON.parseObject(body);
                        JSONObject data = jsonObject.getJSONObject("data");
                        final String orderString = data.getString("orderString");
                        final Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(nopayActivity.this);
                                Map<String, String> result = alipay.payV2(orderString, true);
                                Log.i("msp", result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        // ??????????????????
                        Thread payThread = new Thread(payRunnable);
                        //???????????????
                        payThread.start();
                    }
                }
            });

        }else {
            Toast.makeText(this,"??????????????????????????????",Toast.LENGTH_SHORT).show();

        }


        /*
         * ????????????????????????????????????????????????????????????????????????????????????Demo?????????????????????????????????????????????
         * ??????App??????privateKey??????????????????????????????????????????????????????????????????????????????
         * ????????????????????????????????????????????????????????????????????????????????????????????????
         *
         * orderInfo ?????????????????????????????????
         */
        // ??????????????????

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton("??????", null)
                .setOnDismissListener(onDismiss)
                .show();
    }
    /**
     * ????????????
     */
    private void cancelOrder(String order_no, final int pasation) throws IOException {
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/cancelOrder/" + order_no, "", mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status=OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    mdata.remove(pasation);
                    pdata.remove(pasation);
                    nopayActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            all_chekbox.setChecked(false);
                            tv_total_price.setText("0");
                            noPayAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        });
    }

    /**
     * ?????????????????????
     */
    private  void getInfo() throws IOException {
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/order/noPay", "", mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status=OkhttpUtils.getCode(body);
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
                    nopayActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(orderList.size()!=0){
                                noPayAdapter =new NoPayAdapter(mContext,pdata,mdata);
                                expandableListView.setAdapter(noPayAdapter);
                                expandableListView.expandGroup(0);
                                //???????????????
                                noPayAdapter.setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        int posasion=(int)buttonView.getTag();
                                        if(isChecked){
                                            pdata.get(posasion).put("checked",true);
                                            float price=Float.parseFloat(tv_total_price.getText().toString());
                                            price+=(float)pdata.get(posasion).get("total");
                                            tv_total_price.setText(""+price);
                                        }else{
                                            pdata.get(posasion).put("checked",false);
                                            float price=Float.parseFloat(tv_total_price.getText().toString());
                                            price-=(float)pdata.get(posasion).get("total");
                                            tv_total_price.setText(""+price);
                                        }
                                        noPayAdapter.notifyDataSetChanged();
                                    }
                                });
                                //??????????????????
                                noPayAdapter.setClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final int pasation=(int)v.getTag();
                                        AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                                        builder.setTitle("????????????");
                                        builder.setMessage("???????????????");
                                        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String order_no=(String)pdata.get(pasation).get("orderno");
                                                try {
                                                    cancelOrder(order_no,pasation);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        builder.setNegativeButton("??????",null);
                                        builder.create().show();
                                    }
                                });
                                //???????????????
                                all_chekbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if(isChecked){
                                            for(int i=0;i<pdata.size();i++){
                                                pdata.get(i).put("checked",true);
                                            }
                                            tv_total_price.setText(""+sum());
                                        }else{
                                            for(int i=0;i<pdata.size();i++){
                                                pdata.get(i).put("checked",false);
                                            }
                                            tv_total_price.setText("0");
                                        }
                                        noPayAdapter.notifyDataSetChanged();
                                    }
                                });

                                payBtn.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                             pay();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                    @Override
                                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                        String gid=(String) mdata.get(groupPosition).get(childPosition).get("gid");
                                        Intent intent = new Intent(nopayActivity.this, GoodsInfoActivity.class);
                                        intent.putExtra("gid",gid);
                                        startActivity(intent);
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
    }
    private  void setRemoveList(){
        int sum = 0;
        for (int i= 0 ;i<removeList.size();i++){
            mdata.remove(removeList.get(i)-sum);
            pdata.remove(removeList.get(i)-sum);
            sum++;
        }
    }
}

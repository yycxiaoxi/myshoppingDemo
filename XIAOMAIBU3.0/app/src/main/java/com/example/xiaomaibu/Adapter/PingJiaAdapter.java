package com.example.xiaomaibu.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Bean.Pingjia;
import com.example.xiaomaibu.Dao.DdanxizDao;
import com.example.xiaomaibu.Dao.OrderDao;
import com.example.xiaomaibu.Dao.PingjiaDao;
import com.example.xiaomaibu.PingJiaActivity;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PingJiaAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;//布局加载对象
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private Context context;
    private String uid;
    private Activity activity;
    private View.OnClickListener listener;
    public PingJiaAdapter(Activity activity, Context context, List<Map<String,Object>> pdata, List<List<Map<String,Object>>> mdata,String uid){
        this.pdata=pdata;
        this.mdata=mdata;
        this.context=context;
        this.activity=activity;
        this.uid=uid;
        mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getGroupCount() {
        return pdata.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mdata.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pdata.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mdata.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final  	GroupViewHolder holder;
        holder = new GroupViewHolder();
        convertView=mInflater.inflate(R.layout.activity_ping_jia_plist_item,null);
        holder.pingjia_plist_createtime=convertView.findViewById(R.id.pingjia_plist_createtime);
        holder.pingjia_plist_orderprice=convertView.findViewById(R.id.pingjia_plist_orderprice);
        holder.tvstatus=convertView.findViewById(R.id.status);
        holder.tvtime=convertView.findViewById(R.id.tvtime);
        holder.orderno=convertView.findViewById(R.id.orderno);

        final Map<String,Object> map=pdata.get(groupPosition);
        holder.pingjia_plist_orderprice.setText(map.get("total").toString());
        holder.pingjia_plist_createtime.setText((String)map.get("createtime"));
        holder.orderno.setText((String)map.get("orderno"));

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;

        childViewHolder=new ChildViewHolder();
        convertView=mInflater.inflate(R.layout.activity_ping_jia_clist_item,null);
        childViewHolder.pingjia_listview_item_img=convertView.findViewById(R.id.pingjia_listview_item_img);
        childViewHolder.pingjia_listview_item_name=convertView.findViewById(R.id.pingjia_listview_item_name);
        childViewHolder.pingjia_listview_item_num=convertView.findViewById(R.id.pingjia_listview_item_num);
        childViewHolder.pingjia_listview_item_price=convertView.findViewById(R.id.pingjia_listview_item_price);
        childViewHolder.pingjia_listview_item_total=convertView.findViewById(R.id.pingjia_listview_item_total);
        childViewHolder.pingjia_listview_item_pingjia=convertView.findViewById(R.id.pingjia_listview_item_pingjia);
        childViewHolder.pingjia_listview_item_yipingjia=convertView.findViewById(R.id.pingjia_listview_item_yipingjia);



        final Map<String,Object> childlist=mdata.get(groupPosition).get(childPosition);
        childViewHolder.pingjia_listview_item_name.setText((String)childlist.get("name"));
        Glide.with(context).load(OkhttpUtils.imgIP+childlist.get("img"))
                .into(childViewHolder.pingjia_listview_item_img);
        childViewHolder.pingjia_listview_item_num.setText(childlist.get("num").toString());
        childViewHolder.pingjia_listview_item_price.setText(childlist.get("price").toString());
        childViewHolder.pingjia_listview_item_total.setText(childlist.get("total").toString());
        String status="0";
        try {
            status=childlist.get("status").toString();
        }catch (Exception e){
            e.getStackTrace();
        }

        if(status.equals("1")){
            childViewHolder.pingjia_listview_item_yipingjia.setVisibility(View.VISIBLE);
            childViewHolder.pingjia_listview_item_pingjia.setVisibility(View.GONE);
            childViewHolder.pingjia_listview_item_yipingjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String xizehao=(String) mdata.get(groupPosition).get(childPosition).get("xizehao");
                    try {
                        findPingjia(xizehao);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else
        {
            childViewHolder.pingjia_listview_item_yipingjia.setVisibility(View.GONE);
            childViewHolder.pingjia_listview_item_pingjia.setVisibility(View.VISIBLE);
            childViewHolder.pingjia_listview_item_pingjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle("评价");
                    final EditText editText= new EditText(context);
                    builder.setView(editText);
                    builder.setMessage("请输入你的评价");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String info = editText.getText().toString();
                            String xizehao=(String) mdata.get(groupPosition).get(childPosition).get("xizehao");
                            String gid=(String) mdata.get(groupPosition).get(childPosition).get("gid");
                            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String datetime = tempDate.format(new java.util.Date());
                            Pingjia pingjia=new Pingjia();
                            pingjia.setGid(gid);
                            pingjia.setInfo(info);
                            pingjia.setPingjiano(UUID.randomUUID().toString());
                            pingjia.setTime(datetime);
                            pingjia.setUid(uid);
                            pingjia.setXizehao(xizehao);
                            mdata.get(groupPosition).get(childPosition).put("status","1");
                            try {
                                insertPingjia(pingjia);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.create().show();

                }

            });
        }



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupViewHolder {
        TextView pingjia_plist_createtime;
        TextView orderno;

        TextView tvtime;
        TextView pingjia_plist_orderprice;
        TextView tvstatus;



    }

    private class ChildViewHolder {
        public TextView pingjia_listview_item_name;
        public ImageView pingjia_listview_item_img;
        public TextView pingjia_listview_item_num;
        public TextView pingjia_listview_item_price;
        public TextView pingjia_listview_item_total;
        public Button pingjia_listview_item_pingjia;
        public Button pingjia_listview_item_yipingjia;

    }

    private void jugepingjia(){
        for (int i=0;i<pdata.size();i++){
            boolean flag=true;
            for (int j=0;j<mdata.get(i).size();j++){
               String status=(String) mdata.get(i).get(j).get("status");
               if (status==null){status="0";}
               if (!status.equals("1")){
                   flag=false;
                   break;
               }
            }
            if(flag==true){
                mdata.remove(i);
                pdata.remove(i--);
            }
        }

    }
    /**
     * 通过订单细则查找评价
     */
    private void findPingjia(final String xizehao) throws IOException {

        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/eva/findEvaByOrder/" + xizehao, "", context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body =response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    JSONObject jsonObject = JSON.parseObject(body);
                    String data  =jsonObject.getString("data");
                    final Pingjia pingjia = JSON.parseObject(data,new TypeReference<Pingjia>(){});
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder=new AlertDialog.Builder(context);
                            builder.setTitle("我的评价");
                            final TextView textView= new TextView(context);
                            textView.setText(pingjia.getInfo());
                            builder.setView(textView);
                            builder.create().show();
                        }
                    });
                }
            }
        });
    }

    private void insertPingjia(Pingjia pingjia) throws IOException {
        String json = JSON.toJSONString(pingjia);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/eva/insert", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)){
                    jugepingjia();//在客户端判断是否全部评价
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"评价成功",Toast.LENGTH_SHORT).show();
                            PingJiaAdapter.this.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

}

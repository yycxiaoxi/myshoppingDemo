package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.util.List;
import java.util.Map;

public class daifahuoAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;//布局加载对象
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private Context context;
    private View.OnClickListener shouhuobtnllistener;
    private View.OnLongClickListener onLongClickListener;
    private View.OnClickListener tuihuoBtn;
    public daifahuoAdapter(Context context, List<Map<String,Object>> pdata, List<List<Map<String,Object>>> mdata){
        this.pdata=pdata;
        this.mdata=mdata;
        this.context=context;
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
        convertView=mInflater.inflate(R.layout.daifahuo_parant_item,null);
        convertView.setTag(groupPosition);
        convertView.setOnLongClickListener(onLongClickListener);
        holder.daifahuo_plist_createtime=convertView.findViewById(R.id.daifahuo_plist_createtime);
        holder.daifahuo_plist_orderprice=convertView.findViewById(R.id.daifahuo_plist_orderprice);
        holder.shouhuo=convertView.findViewById(R.id.shouhuo);
        holder.tvstatus=convertView.findViewById(R.id.status);
        holder.orderno=convertView.findViewById(R.id.orderno);

        /*holder.tuihuo =convertView.findViewById(R.id.tuihuo);*/
        holder.tvtime=convertView.findViewById(R.id.tvtime);
        holder.shouhuo.setTag(groupPosition);
        holder.shouhuo.setOnClickListener(shouhuobtnllistener);
       /* holder.tuihuo.setOnClickListener(tuihuoBtn);*/
        holder.shouhuo.setTag(groupPosition);
        final Map<String,Object> map=pdata.get(groupPosition);
        int status=(int)map.get("status");
        switch (status){
            case 1: holder.tvstatus.setText("待发货");
                    holder.tvtime.setText("订单支付时间：");
                    holder.daifahuo_plist_createtime.setText((String)map.get("paytime"));
                    break;
                    case 2: holder.tvstatus.setText("已发货");
                    holder.tvtime.setText("订单发货时间：");
                    holder.daifahuo_plist_createtime.setText((String)map.get("sendtime"));
                    break;
                    case 3: holder.tvstatus.setText("已到货");
                    holder.tvtime.setText("订单到货时间：");
                    holder.tvstatus.setTextColor(Color.rgb(0,128,0));
                    holder.shouhuo.setVisibility(View.VISIBLE);
                    holder.daifahuo_plist_createtime.setText((String)map.get("artime"));
                    break;
        }
        holder.daifahuo_plist_orderprice.setText(map.get("total").toString());
        holder.orderno.setText((String)map.get("orderno"));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;

        childViewHolder=new ChildViewHolder();
        convertView=mInflater.inflate(R.layout.daifahuo_child_item,null);
        childViewHolder.daifahuo_listview_item_img=convertView.findViewById(R.id.daifahuo_listview_item_img);
        childViewHolder.daifahuo_listview_item_name=convertView.findViewById(R.id.daifahuo_listview_item_name);
        childViewHolder.daifahuo_listview_item_num=convertView.findViewById(R.id.daifahuo_listview_item_num);
        childViewHolder.daifahuo_listview_item_price=convertView.findViewById(R.id.daifahuo_listview_item_price);
        childViewHolder.daifahuo_listview_item_total=convertView.findViewById(R.id.daifahuo_listview_item_total);


        final Map<String,Object> childlist=mdata.get(groupPosition).get(childPosition);
        childViewHolder.daifahuo_listview_item_name.setText((String)childlist.get("name"));
        Glide.with(context).load(OkhttpUtils.imgIP+childlist.get("img"))
                .into( childViewHolder.daifahuo_listview_item_img);
        childViewHolder.daifahuo_listview_item_num.setText(childlist.get("num").toString());
        childViewHolder.daifahuo_listview_item_price.setText(childlist.get("price").toString());
        childViewHolder.daifahuo_listview_item_total.setText(childlist.get("total").toString());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupViewHolder {
        TextView daifahuo_plist_createtime;
        Button tuihuo;
        Button shouhuo;
        TextView tvtime;
        TextView daifahuo_plist_orderprice;
        TextView tvstatus;
        TextView orderno;



    }

    private class ChildViewHolder {
        public TextView daifahuo_listview_item_name;
        public ImageView daifahuo_listview_item_img;
        public TextView daifahuo_listview_item_num;
        public TextView daifahuo_listview_item_price;
        public TextView daifahuo_listview_item_total;

    }
    public void setshouhuoBtn(View.OnClickListener shouhuobtnllistener){
        this.shouhuobtnllistener=shouhuobtnllistener;
    }
    public void settuihuoBtn(View.OnClickListener listener){
        this.tuihuoBtn=listener;
    }
    public void setGrouponLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener=onLongClickListener;
    }
}

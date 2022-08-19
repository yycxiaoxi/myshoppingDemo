package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.util.List;
import java.util.Map;

public class AdminSetRefundAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;//布局加载对象
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private View.OnClickListener refund;
    private View.OnClickListener norefund;
    private Context context;
    public AdminSetRefundAdapter(Context context, List<Map<String,Object>> pdata, List<List<Map<String,Object>>> mdata){
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
        final GroupViewHolder holder;
        holder = new GroupViewHolder();
        convertView=mInflater.inflate(R.layout.fragment_admin_fourth_plist_item,null);
        holder.plist_orderprice=convertView.findViewById(R.id.plist_orderprice);
        holder.refundBtn=convertView.findViewById(R.id.refundBtn);
        holder.refundBtn.setTag(groupPosition);
        holder.norefundBtn=convertView.findViewById(R.id.norefundBtn);
        holder.norefundBtn.setTag(groupPosition);
        holder.orderno=convertView.findViewById(R.id.orderno);
        holder.tvinfo=convertView.findViewById(R.id.tvinfo);
        holder.tvtime=convertView.findViewById(R.id.tvtime);

        final Map<String,Object> map=pdata.get(groupPosition);
        holder.plist_orderprice.setText(map.get("total").toString());
        holder.orderno.setText(map.get("orderno").toString());
        holder.tvinfo.setText(map.get("info").toString());
        holder.tvtime.setText(map.get("time").toString());
        holder.refundBtn.setOnClickListener(refund);
        holder.norefundBtn.setOnClickListener(norefund);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;

        childViewHolder=new ChildViewHolder();
        convertView=mInflater.inflate(R.layout.fragment_admin_fourth_clist_item,null);
        childViewHolder.listview_item_img=convertView.findViewById(R.id.listview_item_img);
        childViewHolder.listview_item_name=convertView.findViewById(R.id.listview_item_name);
        childViewHolder.listview_item_num=convertView.findViewById(R.id.listview_item_num);
        childViewHolder.listview_item_price=convertView.findViewById(R.id.listview_item_price);
        childViewHolder.listview_item_total=convertView.findViewById(R.id.listview_item_total);


        final Map<String,Object> childlist=mdata.get(groupPosition).get(childPosition);
        childViewHolder.listview_item_name.setText((String)childlist.get("name"));
        Glide.with(context).load(OkhttpUtils.imgIP+childlist.get("img"))
                .into(childViewHolder.listview_item_img);
        childViewHolder.listview_item_num.setText(childlist.get("num").toString());
        childViewHolder.listview_item_price.setText(childlist.get("price").toString());
        childViewHolder.listview_item_total.setText(childlist.get("total").toString());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupViewHolder {
        TextView plist_createtime;
        Button refundBtn;
        Button norefundBtn;
        TextView plist_orderprice;
        TextView orderno;
        TextView tvinfo;
        TextView status;
        TextView tvtime;


    }

    private class ChildViewHolder {
        public TextView listview_item_name;
        public ImageView listview_item_img;
        public TextView listview_item_num;
        public TextView listview_item_price;
        public TextView listview_item_total;

    }
    public void setRefundListener(View.OnClickListener refund){
        this.refund=refund;
    }
    public void setNorefundListener(View.OnClickListener norefund){
        this.norefund=norefund;
    }
}

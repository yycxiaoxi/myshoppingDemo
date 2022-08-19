package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.util.List;
import java.util.Map;

public class RefundAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;//布局加载对象
    private List<Map<String,Object>> pdata;
    private List<List<Map<String,Object>>> mdata;
    private Context context;
    private View.OnLongClickListener onLongClickListener;
    public RefundAdapter(Context context, List<Map<String,Object>> pdata, List<List<Map<String,Object>>> mdata){
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
        convertView=mInflater.inflate(R.layout.activity_refund_plist_item,null);
        convertView.setTag(groupPosition);
        convertView.setOnLongClickListener(onLongClickListener);
        holder.refund_plist_createtime=convertView.findViewById(R.id.refund_plist_createtime);
        holder.refund_plist_orderprice=convertView.findViewById(R.id.refund_plist_orderprice);
        holder.shouhuo=convertView.findViewById(R.id.shouhuo);
        holder.orderno=convertView.findViewById(R.id.orderno);
        holder.tvtime=convertView.findViewById(R.id.tvtime);
        holder.status=convertView.findViewById(R.id.status);
        holder.tvinfo=convertView.findViewById(R.id.tvinfo);
        holder.layout=convertView.findViewById(R.id.labeled);
        convertView.setTag(groupPosition);
        convertView.setOnLongClickListener(onLongClickListener);

        Log.d("niaho",groupPosition+"");
        final Map<String,Object> map=pdata.get(groupPosition);
        holder.refund_plist_orderprice.setText(map.get("total").toString());
        holder.orderno.setText((String)map.get("orderno"));
        String status=(String) map.get("status");
        if(status.equals("0")){
            holder.tvtime.setText("退款申请时间：");
            holder.status.setText("退款中");
            holder.refund_plist_createtime.setText(map.get("time").toString());
        }else if(status.equals("2"))
        {
            holder.tvtime.setText("商家处理时间：");
            holder.status.setText("商家拒绝退款");
            holder.layout.setVisibility(View.VISIBLE);
            holder.tvinfo.setText(map.get("info").toString());
            holder.refund_plist_createtime.setText(map.get("time").toString());
        }else if(status.equals("1")){
            holder.tvtime.setText("商家处理时间：");
            holder.status.setText("已退款");
            holder.status.setTextColor(android.graphics.Color.parseColor("#006600"));
            holder.refund_plist_createtime.setText(map.get("time").toString());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;

        childViewHolder=new ChildViewHolder();
        convertView=mInflater.inflate(R.layout.activity_refund_clist_item,null);
        childViewHolder.refund_listview_item_img=convertView.findViewById(R.id.refund_listview_item_img);
        childViewHolder.refund_listview_item_name=convertView.findViewById(R.id.refund_listview_item_name);
        childViewHolder.refund_listview_item_num=convertView.findViewById(R.id.refund_listview_item_num);
        childViewHolder.refund_listview_item_price=convertView.findViewById(R.id.refund_listview_item_price);
        childViewHolder.refund_listview_item_total=convertView.findViewById(R.id.refund_listview_item_total);


        final Map<String,Object> childlist=mdata.get(groupPosition).get(childPosition);
        childViewHolder.refund_listview_item_name.setText((String)childlist.get("name"));
        Glide.with(context).load(OkhttpUtils.imgIP+childlist.get("img"))
                .into(childViewHolder.refund_listview_item_img);
        childViewHolder.refund_listview_item_num.setText(childlist.get("num").toString());
        childViewHolder.refund_listview_item_price.setText(childlist.get("price").toString());
        childViewHolder.refund_listview_item_total.setText(childlist.get("total").toString());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class GroupViewHolder {
        LinearLayout layout;
        TextView refund_plist_createtime;
        Button shouhuo;
        TextView tvtime;
        TextView refund_plist_orderprice;
        TextView orderno;
        TextView tvinfo;
        TextView status;


    }

    private class ChildViewHolder {
        public TextView refund_listview_item_name;
        public ImageView refund_listview_item_img;
        public TextView refund_listview_item_num;
        public TextView refund_listview_item_price;
        public TextView refund_listview_item_total;

    }
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener=onLongClickListener;
    }

}

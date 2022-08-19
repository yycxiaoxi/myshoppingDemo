package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Map;

public class NoPayAdapter extends BaseExpandableListAdapter {
    private LayoutInflater mInflater;//布局加载对象
    private Context context;
    private CompoundButton.OnCheckedChangeListener checkedChangeListener;
    private View.OnClickListener clickListener;
    private CompoundButton.OnCheckedChangeListener childClickListener;
    private List<List<Map<String,Object>>> mdata;
    private List<Map<String,Object>> pdata;


    public NoPayAdapter(Context context,List pdata,List mdata){
        this.context=context;
        mInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mdata=mdata;
        this.pdata=pdata;
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
            convertView=mInflater.inflate(R.layout.nopay_parant_list_view,null);
            holder.nopay_plist_createtime=convertView.findViewById(R.id.nopay_plist_createtime);
            holder.nopay_plist_chekbox=convertView.findViewById(R.id.nopay_plist_chekbox);
            holder.nopay_plist_delete=convertView.findViewById(R.id.nopay_plist_delete);
            holder.nopay_plist_orderprice=convertView.findViewById(R.id.nopay_plist_orderprice);
            holder.nopay_plist_chekbox.setTag(groupPosition);
            holder.nopay_plist_delete.setTag(groupPosition);



        final Map<String,Object> map=pdata.get(groupPosition);
        holder.nopay_plist_orderprice.setText(map.get("total").toString());
        holder.nopay_plist_createtime.setText((String)map.get("createtime"));
        holder.nopay_plist_chekbox.setChecked((boolean)map.get("checked"));//初始化控件
        holder.nopay_plist_chekbox.setOnCheckedChangeListener(checkedChangeListener);//设置回调点击事件
        holder.nopay_plist_delete.setOnClickListener(clickListener);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder childViewHolder;

            childViewHolder=new ChildViewHolder();
            convertView=mInflater.inflate(R.layout.nopay_child_item,null);
            childViewHolder.nopay_listview_item_img=convertView.findViewById(R.id.nopay_listview_item_img);
            childViewHolder.nopay_listview_item_name=convertView.findViewById(R.id.nopay_listview_item_name);
            childViewHolder.nopay_listview_item_num=convertView.findViewById(R.id.nopay_listview_item_num);
            childViewHolder.nopay_listview_item_price=convertView.findViewById(R.id.nopay_listview_item_price);
            childViewHolder.nopay_listview_item_total=convertView.findViewById(R.id.nopay_listview_item_total);


        final Map<String,Object> childlist=mdata.get(groupPosition).get(childPosition);
        childViewHolder.nopay_listview_item_name.setText((String)childlist.get("name"));
        Glide.with(context).load(OkhttpUtils.imgIP+childlist.get("img"))
                .into(childViewHolder.nopay_listview_item_img);
        //childViewHolder.nopay_listview_item_img.setImageBitmap((Bitmap)childlist.get("img"));
        childViewHolder.nopay_listview_item_num.setText(childlist.get("num").toString());
        childViewHolder.nopay_listview_item_price.setText(childlist.get("price").toString());
        childViewHolder.nopay_listview_item_total.setText(childlist.get("total").toString());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private class GroupViewHolder {
        TextView nopay_plist_createtime;
        CheckBox nopay_plist_chekbox;
        Button nopay_plist_delete;
        TextView nopay_plist_orderprice;


    }

    private class ChildViewHolder {
        public TextView nopay_listview_item_name;
        public ImageView nopay_listview_item_img;
        public TextView nopay_listview_item_num;
        public TextView nopay_listview_item_price;
        public TextView nopay_listview_item_total;

    }
    public void setCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener){
        this.checkedChangeListener=checkedChangeListener;
    }
    public void setClickListener(View.OnClickListener clickListener){
        this.clickListener=clickListener;
    }
    public void setChildClickListener(CompoundButton.OnCheckedChangeListener childClickListener){
        this.childClickListener=childClickListener;
    }

}

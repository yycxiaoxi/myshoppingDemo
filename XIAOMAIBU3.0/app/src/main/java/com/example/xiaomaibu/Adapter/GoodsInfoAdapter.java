package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.xiaomaibu.Bean.Pingjia;
import com.example.xiaomaibu.R;

import java.util.List;

public class GoodsInfoAdapter extends BaseAdapter {
    private Context context;
    private List<Pingjia> pingjiaList;
    private LayoutInflater mInflater;//布局加载对象
    public  GoodsInfoAdapter(Context context,List<Pingjia> pingjiaList){
        this.pingjiaList=pingjiaList;
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return pingjiaList.size();
    }

    @Override
    public Object getItem(int position) {
        return pingjiaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.goodsinfo_list_item,null);
            viewHolder.datetime=convertView.findViewById(R.id.datetime);
            viewHolder.info=convertView.findViewById(R.id.info);
            viewHolder.user=convertView.findViewById(R.id.user);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.user.setText(pingjiaList.get(position).getUid());
        viewHolder.info.setText(pingjiaList.get(position).getInfo());
        viewHolder.datetime.setText(pingjiaList.get(position).getTime());
        return convertView;
    }
    class ViewHolder{
        public TextView user;
        public TextView info;
        public TextView datetime;
    }
}

package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.AddGoodsActivity;

import java.util.List;

public class AdminBlgAdapter extends BaseAdapter {
    private Context context;
    private List<Goods> goodsList;
    private LayoutInflater mInflater;//布局加载对象
    public  AdminBlgAdapter(Context context,List<Goods> goodsList){
        this.goodsList=goodsList;
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int i) {
        return goodsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();
            view=mInflater.inflate(R.layout.admin_blg_item,null);
            viewHolder.admin_listview_item_img=view.findViewById(R.id.admin_listview_item_img);
            viewHolder.admin_listview_item_name=view.findViewById(R.id.admin_listview_item_name);
            viewHolder.admin_listview_item_price=view.findViewById(R.id.admin_listview_item_price);
            viewHolder.admin_listview_item_cost=view.findViewById(R.id.admin_listview_item_cost);
            viewHolder.admin_listview_item_num=view.findViewById(R.id.admin_listview_item_num);
            viewHolder.admin_listview_item_sort=view.findViewById(R.id.admin_listview_item_sort);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) view.getTag();
        }
        Goods goods=goodsList.get(i);
        Glide.with(context).load(OkhttpUtils.imgIP+goods.getImg())
                .into(viewHolder.admin_listview_item_img);
       // viewHolder.admin_listview_item_img.setImageBitmap(goods.getImg());
        viewHolder.admin_listview_item_name.setText(goods.getName());
        viewHolder.admin_listview_item_price.setText(goods.getPrice()+"");
        viewHolder.admin_listview_item_num.setText(goods.getNum()+"");
        viewHolder.admin_listview_item_sort.setText(goods.getSort());
        viewHolder.admin_listview_item_cost.setText(goods.getCost()+"");
        return view;
    }
    class ViewHolder{
        public TextView admin_listview_item_name;
        public ImageView admin_listview_item_img;
        public TextView admin_listview_item_price;
        public TextView admin_listview_item_num;
        public TextView admin_listview_item_cost;
        public TextView admin_listview_item_sort;

    }
}

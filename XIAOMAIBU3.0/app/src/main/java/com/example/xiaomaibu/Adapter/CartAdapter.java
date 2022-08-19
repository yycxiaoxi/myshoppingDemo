package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.util.List;
import java.util.Map;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> mapList;
    private LayoutInflater mInflater;//布局加载对象
    private View.OnClickListener onAddNum;  //加商品数量接口
    private View.OnClickListener onSubNum;  //减商品数量接口
    private CompoundButton.OnCheckedChangeListener CheckBox;  //减商品数量接口
    public  CartAdapter(Context context, List<Map<String,Object>> mapList){
        this.mapList=mapList;
        mInflater=LayoutInflater.from(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int i) {
        return mapList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
       final ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();
            view=mInflater.inflate(R.layout.cart__list_item,null);
            viewHolder.cart_listview_item_img=view.findViewById(R.id.cart_listview_item_img);
            viewHolder.cart_listview_item_name=view.findViewById(R.id.cart_listview_item_name);
            viewHolder.cart_listview_item_price=view.findViewById(R.id.cart_listview_item_price);
            viewHolder.cart_listview_item_cheak=view.findViewById(R.id.cart_listview_item_cheak);
            viewHolder.cart_listview_item_num=view.findViewById(R.id.cart_listview_item_num);
            viewHolder.onAddNum=view.findViewById(R.id.onAddNum);
            viewHolder.onSubNum=view.findViewById(R.id.onSubNum);
            viewHolder.cart_listview_item_cheak.setOnCheckedChangeListener(CheckBox);//回调方法
            viewHolder.onAddNum.setOnClickListener(onAddNum);
            viewHolder.onSubNum.setOnClickListener(onSubNum);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) view.getTag();
        }
        final Map<String,Object> goodsMap=mapList.get(i);
        Glide.with(context).load(OkhttpUtils.imgIP+goodsMap.get("img"))
                .into(viewHolder.cart_listview_item_img);
        viewHolder.cart_listview_item_name.setText(goodsMap.get("name").toString());
        viewHolder.cart_listview_item_price.setText(goodsMap.get("price").toString());
        viewHolder.cart_listview_item_num.setText(goodsMap.get("cartnum").toString());
        viewHolder.cart_listview_item_cheak.setTag(i);
        viewHolder.cart_listview_item_cheak.setChecked((Boolean) goodsMap.get("checked"));
        viewHolder.onSubNum.setTag(i);
        viewHolder.onAddNum.setTag(i);




        return view;
    }
    class ViewHolder{
        public TextView cart_listview_item_name;
        public ImageView cart_listview_item_img;
        public TextView cart_listview_item_price;
        public CheckBox cart_listview_item_cheak;
        public TextView cart_listview_item_num;
        public Button onAddNum;
        public Button onSubNum;


    }
    public void setOnAddNum(View.OnClickListener onAddNum){
        this.onAddNum = onAddNum;
    }

    public void setOnSubNum(View.OnClickListener onSubNum){
        this.onSubNum = onSubNum;
    }
    public void setCheckBox(CompoundButton.OnCheckedChangeListener CheckBox){
        this.CheckBox=CheckBox;
    }

}

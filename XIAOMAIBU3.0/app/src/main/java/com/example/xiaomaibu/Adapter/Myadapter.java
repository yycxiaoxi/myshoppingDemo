package com.example.xiaomaibu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Bean.Cart;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.CartDao;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.util.List;

public class Myadapter extends BaseAdapter {
    private Context context;
    private List<Goods> goodsList;
    private LayoutInflater mInflater;//布局加载对象
    private String uid;
    public  Myadapter(Context context,List<Goods> goodsList){
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
            view=mInflater.inflate(R.layout.main_listview_item,null);
            viewHolder.main_listview_item_img=view.findViewById(R.id.main_listview_item_img);
            viewHolder.main_listview_item_name=view.findViewById(R.id.main_listview_item_name);
            viewHolder.main_listview_item_price=view.findViewById(R.id.main_listview_item_price);
            viewHolder.main_listview_item_cartbtn=view.findViewById(R.id.main_listview_item_cartbtn);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) view.getTag();
        }
        Goods goods=goodsList.get(i);
        /*viewHolder.main_listview_item_img.setImageBitmap(goods.getImg());*/
        Glide.with(context).load(OkhttpUtils.imgIP + goods.getImg())
                .into(viewHolder.main_listview_item_img);
        viewHolder.main_listview_item_name.setText(goods.getName());
        viewHolder.main_listview_item_price.setText(goods.getPrice()+"");
        viewHolder.main_listview_item_cartbtn.setTag(i);
        viewHolder.main_listview_item_cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gid= goodsList.get((int)view.getTag()).getGid();
                TemploginDao temploginDao=new TemploginDao();
                uid=temploginDao.find(context);
                if(uid==null){
                    Intent intent=new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }else{
                    Cart cart=find(context,gid,uid);
                    if(cart==null){
                        cart=new Cart();
                        cart.setUid(uid);
                        cart.setNum(1);
                        cart.setGid(gid);
                        boolean flag=addcart(context,cart);
                        if(flag){
                            Toast.makeText(context.getApplicationContext(),"添加购物车成功",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context.getApplicationContext(),"添加购物车失败",Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        cart.setNum(cart.getNum()+1);
                        boolean flag=updatecart(context,cart);
                        if(flag){
                            Toast.makeText(context.getApplicationContext(),"添加购物车成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context.getApplicationContext(),"添加购物车失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                }


            }
        });
        return view;
    }
    class ViewHolder{
        public TextView main_listview_item_name;
        public ImageView main_listview_item_img;
        public TextView main_listview_item_price;
        public Button main_listview_item_cartbtn;

    }
    private boolean addcart(Context context,Cart cart){
            CartDao cartDao=new CartDao();
            boolean flag=cartDao.insert(context,cart);
            return flag;

    }
    private boolean updatecart(Context context,Cart cart){
        CartDao cartDao=new CartDao();
        boolean flag=cartDao.update(context,cart);
        return flag;
    }
    private Cart find(Context context,String gid,String uid){
        CartDao cartDao=new CartDao();
        Cart cart=cartDao.find(context,gid,uid);
        return cart;
    }
}

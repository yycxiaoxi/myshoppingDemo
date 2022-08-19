package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CheckBox;

import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public void insert(Context context, Order order){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("insert into \"order\" values(?,?,?,?,?,?,?,?,?,?)",new Object[]{
                    order.getOrderno(),
                    order.getId(),
                    order.getCreatetime(),
                    order.getPaytime(),
                    order.getSendtime(),
                    order.getExpname(),
                    order.getArtime(),
                    order.getAgrtime(),
                    order.getStatus(),
                    order.getTotal(),
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void delete (Context context,String orderno){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("delete from \"order\" where orderno=?",new String[]{orderno});
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public void update(Context context,Order order){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("update \"order\" set id=?,createtime=?,paytime=?,sndtime=?,expname=?,artime=?,agrtime=?,status=?,total=? " +
                    "where orderno=?",new Object[]{
                    order.getId(),
                    order.getCreatetime(),
                    order.getPaytime(),
                    order.getSendtime(),
                    order.getExpname(),
                    order.getArtime(),
                    order.getAgrtime(),
                    order.getStatus(),
                    order.getTotal(),
                    order.getOrderno(),

            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public  Order find(Context context,String orderno){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        Order order=new Order();
        try{
        Cursor cursor = mDb.rawQuery("select * from \"order\" where orderno=?",new String[]{orderno});
        while (cursor.moveToNext()){
            order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
            order.setId(cursor.getString(cursor.getColumnIndex("id")));
            order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
            order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
            order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
            order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
            order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
            order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
            order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
            }
            return  order;
        }catch (Exception e){
            e.getStackTrace();
        }
        return order;
    }
    public List<Order> findallnopay(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where id=? and status=0",new String[]{uid});

            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }
    public List<Order> daifahuo(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where id=? and status=1 and orderno not in(select orderno from Refund)"
                    ,  new String[]{uid});
            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }
    public List<Order> daishouhuo(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where id=? and status=2 and orderno not in(select orderno from Refund)"
                    ,  new String[]{uid});
            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }
    public List<Order> userdaishouhuo(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where id=? and status=2 or status=3  and orderno not in(select orderno from Refund)"
                    ,  new String[]{uid});
            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }
    public List<Order> userdaipingjia(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where id=? and status=4  and orderno not in(select orderno from Refund)"
                    ,  new String[]{uid});
            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }
    public List<Order> userrefund(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where id=? and  orderno in (select orderno from Refund )"
                    ,  new String[]{uid});
            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }
    public List<Order> adminrefund(Context context){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Order> orderList=new ArrayList<>();
        try{
            Cursor cursor= mDb.rawQuery("select * from \"order\" where   orderno in (select orderno from Refund where status='0')"
                    ,null);
            while (cursor.moveToNext()){
                Order order=new Order();
                order.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                order.setId(cursor.getString(cursor.getColumnIndex("id")));
                order.setCreatetime(cursor.getString(cursor.getColumnIndex("createtime")));
                order.setSendtime(cursor.getString(cursor.getColumnIndex("sndtime")));
                order.setExpname(cursor.getString(cursor.getColumnIndex("expname")));
                order.setArtime(cursor.getString(cursor.getColumnIndex("artime")));
                order.setAgrtime(cursor.getString(cursor.getColumnIndex("agrtime")));
                order.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                order.setPaytime(cursor.getString(cursor.getColumnIndex("paytime")));
                order.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                orderList.add(order);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }
        return orderList;
    }

}


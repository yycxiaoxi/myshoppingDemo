package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xiaomaibu.Bean.Cart;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public boolean insert(Context context,Cart cart){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();

        try {
            mDb.execSQL("insert into cart values(?,?,?)",new Object[]{
                    cart.getGid(),
                    cart.getUid(),
                    cart.getNum(),
            });
            return true;
        }catch (Exception e){
            e.getStackTrace();
        }
        return false;
    }
    public Cart find(Context context,String gid,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        Cart cart=null;
        Cursor cursor= null;
        try{
          cursor= mDb.rawQuery("select * from Cart where gid=? and uid=?",new String[]{
                    gid,
                    uid,
            });
          while (cursor.moveToNext()){
              cart=new Cart();
              cart.setGid(cursor.getString(cursor.getColumnIndex("gid")));
              cart.setNum(cursor.getInt(cursor.getColumnIndex("num")));
              cart.setUid(cursor.getString(cursor.getColumnIndex("uid")));

          }
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return  cart;
    }
    public boolean update(Context context,Cart cart){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try{
            mDb.execSQL("update cart set num=? where gid=? and uid=?",new Object[]{
                    cart.getNum(),
                    cart.getGid(),
                    cart.getUid(),
            });
            return true;
        }catch (Exception e){
            e.getStackTrace();
        }
        return false;
    }
    public boolean delete(Context context,String gid,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try{
            mDb.execSQL("delete from cart where gid=? and uid=?",new Object[]{
                    gid,
                    uid,
            });
            return true;
        }catch (Exception e){
            e.getStackTrace();
        }
        return false;
    }
    public List<Cart> findall(Context context,String uid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        List <Cart> cartList=new ArrayList<>();
        Cursor cursor=null;
        try{
            cursor= mDb.rawQuery("select * from Cart where  uid=?",new String[]{
                    uid,
            });
            while (cursor.moveToNext()){
                Cart cart=new Cart();
                cart.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                cart.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                cart.setUid(cursor.getString(cursor.getColumnIndex("uid")));
                cartList.add(cart);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return  cartList;
    }

}

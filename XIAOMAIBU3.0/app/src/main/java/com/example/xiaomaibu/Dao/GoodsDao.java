package com.example.xiaomaibu.Dao;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;
import com.example.xiaomaibu.Util.ImgUtil;

import java.util.ArrayList;
import java.util.List;

public class GoodsDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public boolean insert(Context context, Goods goods){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
       //byte [] imgbyte=ImgUtil.getBytes(goods.getImg());
        try{
            mDb.execSQL("insert into Goods (gid,aid,name,img,price,cost,num,sort)values(?,?,?,?,?,?,?,?)",
                    new Object[]{
                            goods.getGid(),
                            goods.getAid(),
                            goods.getName(),

                            goods.getPrice(),
                            goods.getCost(),
                            goods.getNum(),
                            goods.getSort(),
                    });
        }catch (Exception e){
            Log.d("db",e.getMessage());
            return  false;
        }
        return true;
    }
    public List<Goods> findall(Context context){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        List<Goods> goodsList=new ArrayList<>();
        try{
           Cursor cursor= mDb.rawQuery("select * from Goods",null);
            while (cursor.moveToNext()){
                Goods goods=new Goods();
                goods.setCost(cursor.getFloat(cursor.getColumnIndex("cost")));
                goods.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                goods.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                goods.setName(cursor.getString(cursor.getColumnIndex("name")));
                byte [] img=(cursor.getBlob(cursor.getColumnIndex("img")));
                Bitmap bitmap =ImgUtil.getBitmap(img);

                goods.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
                goods.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                goods.setSort(cursor.getString(cursor.getColumnIndex("sort")));
                goodsList.add(goods);

            }
            cursor.close();
        }catch (Exception e){
            Log.d("db","查询goods失败");
        }
     return goodsList;
    }
    public Goods find(Context context,String gid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        Goods goods=null;
        try{

            Cursor cursor= mDb.rawQuery("select * from Goods where gid=?",new String[]{gid});
            while (cursor.moveToNext()){
                goods=new Goods();
                goods.setCost(cursor.getFloat(cursor.getColumnIndex("cost")));
                goods.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                goods.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                goods.setName(cursor.getString(cursor.getColumnIndex("name")));
                byte [] img=(cursor.getBlob(cursor.getColumnIndex("img")));
                Bitmap bitmap =ImgUtil.getBitmap(img);
                goods.setImg(null);
                //goods.setImg(bitmap);
                goods.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
                goods.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                goods.setSort(cursor.getString(cursor.getColumnIndex("sort")));
            }
            cursor.close();
        }catch (Exception e){
            Log.d("db","查询goods失败");
        }
        return goods;
    }
    public boolean Alter(Context context,Goods goods){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
       //byte [] imgbyte=ImgUtil.getBytes(goods.getImg());
        try{
             mDb.execSQL("update  Goods set aid=?,name=?,img=?,price=?,cost=?,num=?,sort=? where gid=?",new Object[]{
                    goods.getAid(),
                    goods.getName(),
                    //imgbyte,
                     null,
                    goods.getPrice(),
                    goods.getCost(),
                    goods.getNum(),
                    goods.getSort(),
                     goods.getGid(),
            });
             return  true;
        }catch (Exception e){
           e.getStackTrace();
           Log.d("db",e.getMessage());
        }
        return false;
    }
    public boolean Delete(Context context,String gid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();

        try{
            mDb.execSQL("delete from Goods where gid=?",new String[]{gid});
            return  true;
        }catch (Exception e){
            e.getStackTrace();
        }
        return false;
    }
    public List<Goods> findlike(Context context,String like){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        List<Goods> goodsList=new ArrayList<>();
        try{

            Cursor cursor= mDb.rawQuery("select * from Goods where name like ?",new String[]{"%"+like+"%"});
            while (cursor.moveToNext()){
                Goods goods=new Goods();
                goods.setCost(cursor.getFloat(cursor.getColumnIndex("cost")));
                goods.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                goods.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                goods.setName(cursor.getString(cursor.getColumnIndex("name")));
                byte [] img=(cursor.getBlob(cursor.getColumnIndex("img")));
                Bitmap bitmap =ImgUtil.getBitmap(img);
                goods.setImg(null);
                //goods.setImg(bitmap);
                goods.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
                goods.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                goods.setSort(cursor.getString(cursor.getColumnIndex("sort")));
                goodsList.add(goods);

            }
            cursor.close();

        }catch (Exception e){
        }
        return goodsList;
    }
    public List<Goods> findsort(Context context,String sort){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        List<Goods> goodsList=new ArrayList<>();
        try{

            Cursor cursor= mDb.rawQuery("select * from Goods where sort like ?",new String[]{"%"+sort+"%"});
            while (cursor.moveToNext()){
                Goods goods=new Goods();
                goods.setCost(cursor.getFloat(cursor.getColumnIndex("cost")));
                goods.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                goods.setAid(cursor.getString(cursor.getColumnIndex("aid")));
                goods.setName(cursor.getString(cursor.getColumnIndex("name")));
                byte [] img=(cursor.getBlob(cursor.getColumnIndex("img")));
                Bitmap bitmap =ImgUtil.getBitmap(img);
                //goods.setImg(bitmap);
                goods.setPrice(cursor.getFloat(cursor.getColumnIndex("price")));
                goods.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                goods.setSort(cursor.getString(cursor.getColumnIndex("sort")));
                goodsList.add(goods);

            }
            cursor.close();

        }catch (Exception e){
        }
        return goodsList;
    }



}

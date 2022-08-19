package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.Bean.Pingjia;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class PingjiaDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public void insert(Context context, Pingjia pingjia){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("insert into \"pingjia\" values(?,?,?,?,?,?)",new Object[]{
                    pingjia.getPingjiano(),
                    pingjia.getGid(),
                    pingjia.getXizehao(),
                    pingjia.getUid(),
                    pingjia.getInfo(),
                    pingjia.getTime(),
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public  Pingjia find(Context context,String xizehao){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        Pingjia pingjia=new Pingjia();
        try{
            Cursor cursor=mDb.rawQuery("select * from pingjia where ddanxizhao=?",new String[]{xizehao});
            while (cursor.moveToNext()){
                pingjia.setXizehao(cursor.getString(cursor.getColumnIndex("ddanxizhao")));
                pingjia.setUid(cursor.getString(cursor.getColumnIndex("uid")));
                pingjia.setTime(cursor.getString(cursor.getColumnIndex("time")));
                pingjia.setPingjiano(cursor.getString(cursor.getColumnIndex("pingjiano")));
                pingjia.setInfo(cursor.getString(cursor.getColumnIndex("info")));
                pingjia.setGid(cursor.getString(cursor.getColumnIndex("gid")));
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        return pingjia;
    }
    public List<Pingjia> findgid(Context context,String gid){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Pingjia> pingjiaList=new ArrayList<>();
        try{
           Cursor cursor=mDb.rawQuery("select * from pingjia where gid=?",new String[]{gid});
            while (cursor.moveToNext()){
                Pingjia pingjia=new Pingjia();
                pingjia.setXizehao(cursor.getString(cursor.getColumnIndex("ddanxizhao")));
                pingjia.setUid(cursor.getString(cursor.getColumnIndex("uid")));
                pingjia.setTime(cursor.getString(cursor.getColumnIndex("time")));
                pingjia.setPingjiano(cursor.getString(cursor.getColumnIndex("pingjiano")));
                pingjia.setInfo(cursor.getString(cursor.getColumnIndex("info")));
                pingjia.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                pingjiaList.add(pingjia);
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        return pingjiaList;
    }
}

package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.xiaomaibu.Bean.Ddanxiz;
import com.example.xiaomaibu.Bean.Order;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DdanxizDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public void insert(Context context, Ddanxiz ddanxiz){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("insert into \"Ddanxiz\" values(?,?,?,?,?,?,?)",new Object[]{
                    ddanxiz.getXizehao(),
                    ddanxiz.getGid(),
                    ddanxiz.getOrderno(),
                    ddanxiz.getGnum(),
                    ddanxiz.getGprice(),
                    ddanxiz.getTotal(),
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public List<Ddanxiz> findall(Context context,String orderno){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        List<Ddanxiz> ddanxizList=new ArrayList<>();
        try{
           Cursor cursor= mDb.rawQuery("select * from ddanxiz where orderno=?",new String[]{orderno});
            while (cursor.moveToNext()){
                Ddanxiz ddanxiz=new Ddanxiz();
                ddanxiz.setXizehao(cursor.getString(cursor.getColumnIndex("xizehao")));
                ddanxiz.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                ddanxiz.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                ddanxiz.setGnum(cursor.getInt(cursor.getColumnIndex("gnum")));
                ddanxiz.setGprice(cursor.getFloat(cursor.getColumnIndex("gprice")));
                ddanxiz.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                ddanxiz.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                ddanxizList.add(ddanxiz);
            }
            cursor.close();
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            mDb.close();
        }
        return ddanxizList;
    }
    public void delete(Context context,String orderno){
        myDatabaseHelper=new MyDatabaseHelper( context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("delete from ddanxiz where orderno=?",new String[]{orderno});
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public Ddanxiz find(Context context,String xizehao){
        myDatabaseHelper=new MyDatabaseHelper( context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        Ddanxiz ddanxiz=new Ddanxiz();
        try{
           Cursor cursor= mDb.rawQuery("select * from ddanxiz where xizehao=?",new String[]{xizehao});
            while (cursor.moveToNext()){
                ddanxiz.setXizehao(cursor.getString(cursor.getColumnIndex("xizehao")));
                ddanxiz.setGid(cursor.getString(cursor.getColumnIndex("gid")));
                ddanxiz.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                ddanxiz.setGnum(cursor.getInt(cursor.getColumnIndex("gnum")));
                ddanxiz.setGprice(cursor.getFloat(cursor.getColumnIndex("gprice")));
                ddanxiz.setTotal(cursor.getFloat(cursor.getColumnIndex("total")));
                ddanxiz.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        return ddanxiz;
    }
    public void update (Context context,String xizehao){
        myDatabaseHelper=new MyDatabaseHelper( context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try {
            mDb.execSQL("update ddanxiz set status=? where xizehao =?",new String[]{"1",xizehao});
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}

package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.xiaomaibu.Bean.Refund;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;


public class RefundDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public   void insert(Context context, Refund refund){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("insert into refund values(?,?,?,?,?)",new Object[]{
                    refund.getRefundno(),
                    refund.getOrderno(),
                    refund.getInfo(),
                    refund.getTime(),
                    refund.getStatus()
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public void delete(Context context,String orderno){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("delete from refund where orderno=?",new String[]{orderno});
        }catch (Exception e){
            e.getStackTrace();
        }
    }
    public   Refund find(Context context,String orderno){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            Cursor cursor =mDb.rawQuery("select * from refund where orderno=?",new String[]{orderno});
            while (cursor.moveToNext()){
                Refund refund=new Refund();
                refund.setInfo(cursor.getString(cursor.getColumnIndex("info")));
                refund.setOrderno(cursor.getString(cursor.getColumnIndex("orderno")));
                refund.setRefundno(cursor.getString(cursor.getColumnIndex("refundno")));
                refund.setTime(cursor.getString(cursor.getColumnIndex("time")));
                refund.setStatus(cursor.getString(cursor.getColumnIndex("status")));
               return refund;
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }
    public  void update (Context context,Refund refund){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getReadableDatabase();
        try{
            mDb.execSQL("update refund set time=?,info=?,status=? where orderno=?",new Object[]{
                    refund.getTime(),
                    refund.getInfo(),
                    refund.getStatus(),
                    refund.getOrderno(),
            });
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}

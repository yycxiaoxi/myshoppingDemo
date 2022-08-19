package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.xiaomaibu.Bean.Admin;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

public class AdminDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public  boolean Login(Context context, Admin admin){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try{
           Cursor cursor= mDb.rawQuery("select * from Admin where aid=?",new String[]{admin.getId()});
            while (cursor.moveToNext()){
                String password=cursor.getString(cursor.getColumnIndex("password"));
                if(password.trim().equals(admin.getPwd().trim())){
                    return true;
                }
            }
            cursor.close();
        }catch (Exception e) {

            return  false;
        }
        return false;
    }
}

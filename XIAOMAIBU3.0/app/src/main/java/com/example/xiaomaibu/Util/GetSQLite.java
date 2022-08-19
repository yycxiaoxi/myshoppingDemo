package com.example.xiaomaibu.Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

public class GetSQLite {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public String getToken(Context context){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();

        try{
            Cursor cursor = mDb.rawQuery("select Authorization from TOKEN",null);
            while (cursor.moveToNext()){
                String token =cursor.getString(cursor.getColumnIndex("Authorization"));
                return token;
            }
            cursor.close();
        }catch (Exception e){
            Log.d("db",e.getMessage());
            return "123456" ;
        }
        return  "123456";
    }
    public void setToken(String token,Context context){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        mDb.execSQL("update TOKEN set Authorization = ?",new String[]{token});
    }
}

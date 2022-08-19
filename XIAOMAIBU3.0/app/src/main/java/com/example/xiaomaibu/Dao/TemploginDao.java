package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xiaomaibu.JDBC.MyDatabaseHelper;

public class TemploginDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public void insert(Context context,String id){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try {
            mDb.execSQL("insert into templogin values(?)",new String[]{id});
        }catch (Exception e){
            e.getStackTrace();
        }
    };
    public String find(Context context){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        Cursor cursor=null;
        try{
           cursor=mDb.rawQuery("select * from templogin ",null);
           while (cursor.moveToNext()){
               return cursor.getString(cursor.getColumnIndex("id"));
           }
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            if (cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
        }

        return null;
    }
    public void delete(Context context){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try{
            mDb.execSQL("delete from templogin");
        }catch (Exception e){
            e.getStackTrace();
        }
    }

}

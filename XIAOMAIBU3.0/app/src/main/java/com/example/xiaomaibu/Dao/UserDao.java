package com.example.xiaomaibu.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xiaomaibu.Bean.User;
import com.example.xiaomaibu.JDBC.MyDatabaseHelper;




public class UserDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase mDb;
    public boolean Regiter(Context context, User user){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try{
            mDb.execSQL("insert into   User values(?,?,?,?,?,?)",
                    new String[]{
                            user.getId(),
                            user.getName(),
                            user.getPassword(),
                            user.getPnum(),
                            user.getDress(),
                            user.getEmail(),
                    });
        }catch (Exception e) {

            return  false;
        }
        return true;
    }
    public boolean Login(Context context,String id,String pwd){
        myDatabaseHelper=new MyDatabaseHelper(context,"memento.db",null,1);
        mDb=myDatabaseHelper.getWritableDatabase();
        try{

            Cursor cursor =mDb.rawQuery("select * from User where id=?",new String[]{id});
            while (cursor.moveToNext()){
               String password=cursor.getString((cursor.getColumnIndex("password")));
                if(password.equals(pwd)){
                    return true;
                }
            }

        }catch (Exception e){
            Log.d("db",e.getMessage());
            return  false;
        }
        return false;
    }
}

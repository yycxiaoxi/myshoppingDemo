package com.example.xiaomaibu.JDBC;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.xiaomaibu.Bean.Ddanxiz;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    private String CREATE_TABLE_SQL="create table \"User\" (\n" +
            "id                   VARCHAR(30)                    not null," +
            "name                 CHAR(10)," +
            "password             VARCHAR(30)," +
            "pnum                 CHAR(11)," +
            "dress                VARCHAR(50)," +
            "email                 VARBINARY(255)," +
            "primary key (id)" +
            ")";
    private String CREATE_GOODS_SQL="create table \"Goods\"(gid,aid,name,img,price,cost,num,sort,primary key(gid))";
    private String CREATE_ADMIN_SQL="create table \"Admin\"(aid,name,password,primary key(aid))";
    private String CREATE_CRRT_SQL="create table \"Cart\"(gid,uid,num,primary key(gid))";
    private String CREATE_ORDER_SQL="create table \"Order\"(orderno,id,createtime,paytime,sndtime,expname" +
            ",artime,agrtime,status,total,primary key(orderno))";
    private String CREATE_DDANXIZ_SQL="create table \"Ddanxiz\"(xizehao,gid,orderno,gnum,gprice" +
            ",total,status,primary key(xizehao))";

    private String addAdmin="insert into admin values(\"123456\",\"管理员\",\"123456\")";

    private String CREATE_PINGJIA_SQSL="create table \"Pingjia\"(pingjiano,gid,ddanxizhao,uid,info,time,primary key(pingjiano))";

    private String CREATE_TEMPLOGIN_SQL="create table \"templogin\"(id,primary key(id))";

    private String CREATE_TUIKUAN_SQL="create table \"refund\" (refundno,orderno,info,time,status,primary key(refundno))";

    private String CREATE_TOKEN_SQL="create table \"TOKEN\"(Authorization,primary key(Authorization))";
    private String INSERT_TOKEN_SQL="insert into TOKEN values(\"123456\")";

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
        db.execSQL(CREATE_GOODS_SQL);
        db.execSQL(CREATE_ADMIN_SQL);
        db.execSQL(CREATE_CRRT_SQL);
        db.execSQL(CREATE_ORDER_SQL);
        db.execSQL(CREATE_DDANXIZ_SQL);
        db.execSQL(CREATE_PINGJIA_SQSL);
        db.execSQL(CREATE_TEMPLOGIN_SQL);
        db.execSQL(CREATE_TUIKUAN_SQL);
        db.execSQL(addAdmin);
        db.execSQL(CREATE_TOKEN_SQL);
        db.execSQL(INSERT_TOKEN_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("----"+oldVersion+"====>"+newVersion);

    }

}

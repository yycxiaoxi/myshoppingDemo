package com.example.xiaomaibu.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayOutputStream;

public class ImgUtil {
    //Drawable对象转换为byte数组
    public static byte[] getBytes(Bitmap bitmap){
        //将Drawable对象转换为bitmap对象
        //创建输出字节流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //压缩
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, bos);
        return bos.toByteArray();
    }

    public static Bitmap getBitmap(byte[] bytes){
        //BitmapFactory
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }


}

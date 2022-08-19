package com.example.xiaomaibu.goods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.Dao.UserDao;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.ImgUtil;

import java.util.UUID;

public class AddGoodsActivity extends AppCompatActivity {
    private EditText name;
    private EditText price;
    private EditText cost;
    private EditText num;
    private EditText sort;
    private Button img;
    private Button submit;
    private ImageView iv_goods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        name = findViewById(R.id.add_goods_name);
        price = findViewById(R.id.add_goods_price);
        cost = findViewById(R.id.add_goods_cost);
        num = findViewById(R.id.add_goods_num);
        sort = findViewById(R.id.add_goods_sort);
        img = findViewById(R.id.add_goods_img);
        submit = findViewById(R.id.add_goods_btn);
        iv_goods = findViewById(R.id.iv_goods);
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceType", "NewApi"})
            @Override
            public void onClick(View view) {
                Goods goods = new Goods();
                goods.setGid(UUID.randomUUID().toString());
                goods.setAid("123456");
                goods.setName(name.getText().toString());
                goods.setPrice(Integer.parseInt(price.getText().toString()));
                goods.setNum(Integer.parseInt(num.getText().toString()));
                goods.setCost(Integer.parseInt(cost.getText().toString()));
                goods.setSort(sort.getText().toString());
                //插入图片

                Bitmap bm =((BitmapDrawable) (iv_goods).getDrawable()).getBitmap();
                /* Bitmap bitmap=((BitmapDrawable) iv_goods.getDrawable().getCurrent()).getBitmap();*/
                //goods.setImg(bm);

                GoodsDao goodsDao = new GoodsDao();


                Boolean flag = goodsDao.insert(AddGoodsActivity.this, goods);
                if (flag) {
                    Intent intent=getIntent();
                    setResult(0x11,intent);
                    Toast.makeText(AddGoodsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddGoodsActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从手机相册中获取图片需要动态申请权限
                if (ContextCompat.checkSelfPermission(AddGoodsActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddGoodsActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x11);
                } else {
                    //如果已经获取权限，那么就直接拿
                    takePhoto();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0x11:
                takePhoto();
                break;


        }
    }

    private void takePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, 0x11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x11:
                Uri uri = data.getData();
                if (uri != null) {
//                    将图片显示在ImageView
                    iv_goods.setVisibility(View.VISIBLE);
                    Log.d("db", "onActivityResult: "+uri);
                    //根据路径加载图片
                    Glide.with(AddGoodsActivity.this).load(uri).into(iv_goods);
                }
                break;

        }
    }
}

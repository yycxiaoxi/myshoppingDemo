package com.example.xiaomaibu.AdminMainBlg;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.example.xiaomaibu.Bean.Goods;
import com.example.xiaomaibu.Dao.GoodsDao;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.DcimUriget;
import com.example.xiaomaibu.Util.OkhttpUtils;
import com.example.xiaomaibu.goods.AddGoodsActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AlterGoodsBlgActivity extends AppCompatActivity {
    private EditText name;
    private EditText price;
    private EditText cost;
    private EditText num;
    private EditText sort;
    private Button img;
    private Button submit;
    private ImageView iv_goods;
    private Context context;
    private Goods goods;
    private TextView tv_img;
    private static File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_goods_blg);
        name=findViewById(R.id.alter_goods_name);
        price=findViewById(R.id.alter_goods_price);
        cost=findViewById(R.id.alter_goods_cost);
        num=findViewById(R.id.alter_goods_num);
        sort=findViewById(R.id.alter_goods_sort);
        img=findViewById(R.id.alter_goods_blg_img);
        submit=findViewById(R.id.alter_goods_blg_btn);
        iv_goods=findViewById(R.id.alter_goods_iv);
        tv_img=findViewById(R.id.tv_img);
        context=AlterGoodsBlgActivity.this;
        Intent intent=getIntent();
        final String gid= intent.getStringExtra("gid");
        try {
            getGoods(gid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    goods.setGid(gid);
                    goods.setAid("123456");
                    goods.setName(name.getText().toString());
                    goods.setPrice(Float.parseFloat(price.getText().toString()));
                    goods.setNum(Integer.parseInt(num.getText().toString()));
                    goods.setCost(Float.parseFloat(cost.getText().toString()));
                    goods.setSort(sort.getText().toString());
                    goods.setImg(tv_img.getText().toString());
                    //获取照片
                    //Bitmap bm =((BitmapDrawable) (iv_goods).getDrawable()).getBitmap();
                    //goods.setImg(bm);
                try {
                    updateGoods();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });
        //上传照片按钮
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从手机相册中获取图片需要动态申请权限
                if (ContextCompat.checkSelfPermission(AlterGoodsBlgActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AlterGoodsBlgActivity.this,
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
                try{
                    Uri uri = data.getData();
                    if (uri != null) {
//                    将图片显示在ImageView
                        iv_goods.setVisibility(View.VISIBLE);
                        //
                        String imgpath=DcimUriget.getFilePathByUri(context,uri);
                        Log.e("uri",imgpath);
                        Glide.with(AlterGoodsBlgActivity.this).load(uri).into(iv_goods);
                        tv_img.setText(UUID.randomUUID().toString()+".jpg");
                        file= new File(imgpath);
                        sendToServer();

                    }
                }catch (Exception e){

                }

                break;

        }
    }

    private void getGoods(String gid) throws IOException {
        OkhttpUtils.httpGet(OkhttpUtils.IP + "/goods/find/" + gid, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)) {
                    JSONObject jsonObject = JSON.parseObject(body);
                    String data = jsonObject.getString("data");
                    goods = JSON.parseObject(data, new TypeReference<Goods>() {});
                    AlterGoodsBlgActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name.setText(goods.getName());
                            price.setText(goods.getPrice()+"");
                            cost.setText(goods.getCost()+"");
                            num.setText(goods.getNum()+"");
                            sort.setText(goods.getSort());
                            Glide.with(context).load(OkhttpUtils.imgIP+goods.getImg()).into(iv_goods);
                            tv_img.setText(goods.getImg());
                        }
                    });

                }
            }
        });
    }

    private void updateGoods() throws IOException {
        String json = JSON.toJSONString(goods);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/goods/update", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body =response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)) {
                    JSONObject jsonObject = JSON.parseObject(body);
                    String data = jsonObject.getString("data");
                    goods = JSON.parseObject(data, new TypeReference<Goods>() {});
                    AlterGoodsBlgActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlterGoodsBlgActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    AlterGoodsBlgActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AlterGoodsBlgActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
    public void sendToServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                //所有图片类型
                MediaType mediaType= MediaType.parse("image/*;charset=utf-8");
                //第一层，说明数据为文件，以及文件类型
                RequestBody fileBody= RequestBody.create(mediaType, file);

                //第二层，指明服务表单的键名，文件名，文件体
                final RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("myfile",file.getName(),fileBody)
                        .build();
                Request request = new Request.Builder()
                        .url(OkhttpUtils.IP+"/Upload")
                        .post(requestBody)
                        .build();
                //发送请求
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure( Call call, IOException e) {
                        //网络故障
                        Log.e("uri",e.getMessage());
                        Looper.prepare();
                        Toast.makeText(AlterGoodsBlgActivity.this,"网络故障！",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body =response.body().string();
                        String status = OkhttpUtils.getCode(body);
                        if ("200".equals(status)){
                            JSONObject jsonObject =JSON.parseObject(body);
                            final String filename=jsonObject.getString("data");
                            AlterGoodsBlgActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AlterGoodsBlgActivity.this,"上传成功！",Toast.LENGTH_SHORT).show();
                                    tv_img.setText(filename);
                                }
                            });
                        }
                    }
                });


            }
        }).start();
    }

}

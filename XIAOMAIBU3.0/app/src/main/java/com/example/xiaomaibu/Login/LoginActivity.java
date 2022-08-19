package com.example.xiaomaibu.Login;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.example.xiaomaibu.Bean.User;
import com.example.xiaomaibu.Dao.TemploginDao;
import com.example.xiaomaibu.Dao.UserDao;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.GetSQLite;

import com.example.xiaomaibu.Util.OkhttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private TextView email;
    private TextView password;
    private Button submit;
    private Context mContext;
    private GetSQLite getSQLite;
    private TemploginDao temploginDao;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.user_login_id);
        password=findViewById(R.id.user_login_pwd);
        submit=findViewById(R.id.email_sign_in_button);
        mContext=LoginActivity.this;
        getSQLite = new GetSQLite();
        temploginDao= new TemploginDao();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    user=new User();
                    String em= email.getText().toString();
                    String pwd=password.getText().toString();
                    user.setId(em);
                    user.setPassword(pwd);
                    try {
                        Login(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    /**
     *
     * 用户登录
     * @param user
     * @throws IOException
     */
    private void Login(final User user) throws IOException {
        String json = JSON.toJSONString(user);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/user/login", json, mContext, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                JSONObject jsonObject= JSON.parseObject(body);
                Log.d("jsonObject",jsonObject.toJSONString());
                String status = jsonObject.getString("code");
                if (status.equals("200")){
                    String token = jsonObject.getString("data");
                    //登录成功则修改token 每次请求的时候都携带token
                    getSQLite.setToken(token,mContext);
                    temploginDao.insert(mContext,user.getId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LoginActivity.this.finish();
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this,"密码错误或账号不存在",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });
    }
}

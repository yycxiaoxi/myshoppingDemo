package com.example.xiaomaibu.Regiter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.xiaomaibu.Bean.User;
import com.example.xiaomaibu.Dao.UserDao;
import com.example.xiaomaibu.Login.LoginActivity;
import com.example.xiaomaibu.R;
import com.example.xiaomaibu.Util.OkhttpUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegiterActivity extends AppCompatActivity {
    private String id;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText pnum;
    private EditText dress;
    private User user;
    private Button regitersubmit;
    private Context context;
    private Button bt_confirm;
    private EditText et_confirm;
    private Handler handler= new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what==0x11){
                bt_confirm.setEnabled(false);
                bt_confirm.setText((int)msg.obj+"s 后重新获取");
            }
            if (msg.what==0x12){
                bt_confirm.setEnabled(true);
                bt_confirm.setText("点击获取验证码");
            }
        }
    };;
    private String confirmCode="";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiter);
        email=findViewById(R.id.email);
        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        pnum=findViewById(R.id.pnum);
        dress=findViewById(R.id.dress);
        bt_confirm=findViewById(R.id.bt_confirm);
        et_confirm=findViewById(R.id.et_confirm);
        regitersubmit=findViewById(R.id.regitersubmit);
        context=RegiterActivity.this;


        regitersubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("")){
                    Toast.makeText(RegiterActivity.this,"账号不能为空",Toast.LENGTH_SHORT).show();
                } else if(password.getText().toString().equals("")){
                    Toast.makeText(RegiterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(name.getText().toString().equals("")){
                    Toast.makeText(RegiterActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                }else if(pnum.getText().toString().equals("")){
                    Toast.makeText(RegiterActivity.this,"电话不能为空",Toast.LENGTH_SHORT).show();
                }else if(dress.getText().toString().equals("")){
                    Toast.makeText(RegiterActivity.this,"地址不能为空",Toast.LENGTH_SHORT).show();

                }else if (et_confirm.getText().toString().equals("")){
                    Toast.makeText(RegiterActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                } else if (!et_confirm.getText().toString().equals(confirmCode)){
                    Toast.makeText(RegiterActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                }

                else {
                    user = new User();
                    String id[] = email.getText().toString().split("@");
                    user.setId(id[0]);
                    user.setEmail(email.getText().toString());
                    user.setName(name.getText().toString());
                    user.setPassword(password.getText().toString());
                    user.setPnum(pnum.getText().toString());
                    user.setDress(dress.getText().toString());
                    try {
                        register();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmail(email.getText().toString())){
                    Toast.makeText(RegiterActivity.this,"请输入邮箱",Toast.LENGTH_SHORT).show();
                }else{
                    User user =new User();
                    user.setEmail(email.getText().toString());
                    btConfirm();
                    try {
                        getConfirm(user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    /**
     * 注册
     * @throws IOException
     */
    private void register() throws IOException {
        String json = JSON.toJSONString(user);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/user/register", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                String status = OkhttpUtils.getCode(body);
                if ("200".equals(status)) {
                    RegiterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegiterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegiterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    RegiterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegiterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    /**
     * 获取验证码按钮
     */
    private void btConfirm(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                int curr=60;
                while (curr>0){
                    Message message=new Message();
                    message.what=0x11;
                    message.obj=curr;
                    handler.sendMessage(message);
                    curr--;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message message = new Message();
                message.what = 0x12;
                handler.sendMessage(message);

            }
        }.start();
    }

    /**
     * 向服务器请求验证码
     *
     */
    private void getConfirm(User user) throws IOException {

        String json = JSON.toJSONString(user);
        Log.e("TAG",json);
        OkhttpUtils.httpJsonPost(OkhttpUtils.IP + "/user/confirm", json, context, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body=response.body().string();
                String status = OkhttpUtils.getCode(body);
                if("200".equals(status)){
                    JSONObject jsonObject=JSON.parseObject(body);
                    confirmCode=jsonObject.getString("data");
                    RegiterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegiterActivity.this,"获取验证码成功",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

    }
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}

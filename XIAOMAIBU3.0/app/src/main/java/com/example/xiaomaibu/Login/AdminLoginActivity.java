package com.example.xiaomaibu.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiaomaibu.AdminMainBlg.AdminBlgActivity;
import com.example.xiaomaibu.Bean.Admin;
import com.example.xiaomaibu.Dao.AdminDao;
import com.example.xiaomaibu.MainActivity;
import com.example.xiaomaibu.R;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText id;
    private EditText pwd;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        id=findViewById(R.id.admin_login_id);
        pwd=findViewById(R.id.admin_login_pwd);
        login=findViewById(R.id.admin_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Admin admin=new Admin();
                admin.setId(id.getText().toString());
                admin.setPwd(pwd.getText().toString());
                AdminDao adminDao=new AdminDao();
                boolean flag=adminDao.Login(AdminLoginActivity.this,admin);
                if(flag){
                    Intent intent=new Intent(AdminLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(AdminLoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(AdminLoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

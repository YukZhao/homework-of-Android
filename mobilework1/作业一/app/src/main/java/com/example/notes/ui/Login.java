package com.example.notes.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.example.notes.BaseActivity;
import com.example.notes.R;

public class Login extends BaseActivity {
    private EditText username, password;
    private ProgressBar progressBar;
    private int progressValue = 0; // 初始进度值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        titleBar.setTitle("登录");
        titleBar.setDisplayLeftView(false);

        username = findViewById(R.id.et_user_name);
        password = findViewById(R.id.et_psw);
        progressBar = findViewById(R.id.progressBar);

        String name = SPUtils.getInstance().getString("username");
        String pw = SPUtils.getInstance().getString("password");
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pw)) {
            username.setText(name);
            password.setText(pw);
        }
        progressBar = findViewById(R.id.progressBar); // 初始化ProgressBar对象
        progressBar.setMax(100); // 设置最大进度值为100
        //点击登录
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //去注册
        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                intent.putExtra("flag", "login");
                startActivity(intent);
            }
        });
    }

    public void login() {
        String name = username.getText().toString();
        String pw = password.getText().toString();

        if (name.equals("") || pw.equals("")) {
            Toast.makeText(Login.this, "请输入账号密码！", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from user where username=?", new String[]{name});
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                Toast.makeText(Login.this, "不存在该用户！", Toast.LENGTH_SHORT).show();
            } else {
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
                if (pw.equals(password)) {
//                    SPUtils.getInstance().put("username", name);
//                    SPUtils.getInstance().put("password", pw);
//                    Toast.makeText(Login.this,"登录成功！",Toast.LENGTH_SHORT).show();
//                    Intent intent=new Intent(Login.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
                    loginWithProgressBar();
                } else {
                    Toast.makeText(Login.this, "密码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void loginWithProgressBar() {
        // 模拟登录操作，并在主线程中更新进度
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progressValue < 100) {
                    progressValue += 10; // 假设每次增加10作为示例
                    progressBar.setProgress(progressValue); // 更新进度条的显示
                    loginWithProgressBar(); // 继续模拟登录进度
                } else {
                    // 登录完成后的操作
                    // 保存用户名到SharedPreferences
                    String name = username.getText().toString();
                    String pw = password.getText().toString();
                    SPUtils.getInstance().put("username", name);
                    SPUtils.getInstance().put("password", pw);

                    Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_SHORT).show();

                    // 跳转到下一个界面
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // 关闭当前界面
                }
            }
        }, 1000); // 假设每次增加需要1秒钟
    }
}
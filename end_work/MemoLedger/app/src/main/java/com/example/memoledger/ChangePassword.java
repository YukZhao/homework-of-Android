package com.example.memoledger;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import com.example.memoledger.Bean.User;
import com.example.memoledger.Database.UserTB;

import java.util.List;


public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;
    private UserTB userDB;
    String id=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        userDB = new UserTB(this).open(); // 初始化数据库操作类
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        oldPasswordEditText = findViewById(R.id.old_password);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmNewPasswordEditText = findViewById(R.id.confirm_new_password);
        Button changePasswordButton = findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_password_button:
                String oldPassword = oldPasswordEditText.getText().toString();
                String newPassword = newPasswordEditText.getText().toString();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

                // 检查新密码和确认新密码是否相同
                if (newPassword.equals(confirmNewPassword)) {
                    String username = id;

                    // 验证旧密码是否正确
                    List<User> users = userDB.selectByAccountAndPass(username, oldPassword);
                    if (users != null && !users.isEmpty()) {
                        // 旧密码验证通过，执行密码修改操作
                        if (userDB.updatePassword(username, newPassword) > 0) {
                            // 密码修改成功
                            Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            finish(); // 关闭当前界面
                        } else {
                            // 密码修改失败
                            Toast.makeText(this, "密码修改失败，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // 旧密码验证失败
                        Toast.makeText(this, "旧密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 新密码和确认新密码不匹配
                    Toast.makeText(this, "新密码和确认新密码不匹配", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDB != null) {
            userDB.close(); // 关闭数据库连接
        }
    }
}

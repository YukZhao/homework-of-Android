package com.example.memoledger;

import static java.lang.System.exit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalInformation extends Activity implements View.OnClickListener {
    TextView tvUserId;
    ImageView user_img;
    TextView tv_usercenter;
    Button btn_out;
    Button btn_change;
    Button chang_pass;
    String id=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        tvUserId = findViewById(R.id.tv_username);
        // 将用户ID显示在TextView中
        tvUserId.setText("User ID: " + id);

        tv_usercenter = findViewById(R.id.tv_usercenter);
        user_img = findViewById(R.id.user_img);
        btn_out = findViewById(R.id.btn_out);
        btn_change = findViewById(R.id.btn_change);
        chang_pass = findViewById(R.id.chang_pass);
        btn_out.setOnClickListener(this);
        btn_change.setOnClickListener(this);
        chang_pass.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        switch (v.getId()) {
            case R.id.btn_out:
                exit(0);
                break;
            case R.id.btn_change:
                intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // 结束当前Activity
                finish();
                break;
            case R.id.chang_pass:
                intent = new Intent(this, ChangePassword.class);
                intent.putExtra("id",id);
                startActivity(intent);
                // 结束当前Activity
                break;
        }
    }
}

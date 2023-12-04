package com.example.memoledger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomepageActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvUserId;
    String id =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent=getIntent();
        id = intent.getStringExtra("id");

        tvUserId = findViewById(R.id.user_name);
        // 将用户ID显示在TextView中
        tvUserId.setText("User ID: " + id);


        findViewById(R.id.btn_accounting).setOnClickListener(this);
        findViewById(R.id.btn_check_record).setOnClickListener(this);
        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.btn_budget).setOnClickListener(this);
        findViewById(R.id.user_name).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent=null;
        switch (view.getId()){
            case R.id.btn_accounting:
                intent = new Intent(this, ManageActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_record:
                intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_budget:
                intent = new Intent(this, SearchRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_check_record:
                intent = new Intent(this, NotebookActivity.class);
                startActivity(intent);
                break;
            case R.id.user_name:
                intent = new Intent(this, PersonalInformation.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;
        }
    }
}
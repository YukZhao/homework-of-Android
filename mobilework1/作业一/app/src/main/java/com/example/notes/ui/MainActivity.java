package com.example.notes.ui;

import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ViewAnimator;

import com.example.notes.BaseActivity;
import com.example.notes.adapter.NoteAdapter;
import com.example.notes.bean.Notes;
import com.example.notes.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private List<Notes> noteList=new ArrayList<>();
    private ListView listView;
    private ViewAnimator viewAnimator;
    private NoteAdapter noteAdapter;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewAnimator = findViewById(R.id.viewAnimator);
        // 设置欢迎页面的切换动画（可选）
        viewAnimator.setInAnimation(this, android.R.anim.fade_in);
        viewAnimator.setOutAnimation(this, android.R.anim.fade_out);
        viewAnimator.setVisibility(View.VISIBLE);
        viewAnimator.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 切换到下一个视图
                viewAnimator.showNext();
                // 再次延迟一段时间后执行消失操作
                viewAnimator.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 将ViewAnimator设置为不可见，实现欢迎页面的消失
                        viewAnimator.setVisibility(View.GONE);
                    }
                }, 2000); // 2000毫秒后消失，可以根据需求调整时间
            }
        }, 2000);

        titleBar.setTitle("我的记事本");
        titleBar.getRightTextView().setText("新增");
        titleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNote.class));
            }
        });
        listView=findViewById(R.id.listView);
        searchView=findViewById(R.id.searchView);

        //初始化适配器
        noteAdapter=new NoteAdapter(this,R.layout.note_item, noteList);
        //设置适配器
        listView.setAdapter(noteAdapter);


        //搜索笔记
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)){
                    noteList.clear();
                    noteList.addAll(dbOpenHelper.getNotes(query));
                    noteAdapter.notifyDataSetChanged();
                    searchView.clearFocus();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    noteList.clear();
                    noteList.addAll(dbOpenHelper.getNotes(null));
                    noteAdapter.notifyDataSetChanged();
                    searchView.clearFocus();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteList.clear();
        noteList.addAll(dbOpenHelper.getNotes(null));
        noteAdapter.notifyDataSetChanged();
    }
}
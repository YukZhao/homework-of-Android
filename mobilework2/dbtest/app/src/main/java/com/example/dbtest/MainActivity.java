package com.example.dbtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                Uri.parse("content://com.example.dbtest.CourseProvider/course"),
                null,
                null,
                null,
                null
        );
        if (cursor == null) {
            Toast.makeText(this, "请先启动db程序", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        List<String> courseList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int courseIndex = cursor.getColumnIndex("courseName");
            String course = cursor.getString(courseIndex);
            courseList.add(course);
        }
        cursor.close();

        ListView listView = findViewById(R.id.lv);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseList);
        listView.setAdapter(adapter);

        Toast.makeText(this, "长按课程名删除课程", Toast.LENGTH_SHORT).show();

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // prompt user to delete
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Course");
            builder.setMessage("Are you sure you want to delete this course?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                TextView tv = (TextView) view;
                String courseName = tv.getText().toString();
                courseList.remove(courseName);
                adapter.notifyDataSetChanged();
                resolver.delete(
                        Uri.parse("content://com.example.dbtest.CourseProvider/course"),
                        "courseName=?",
                        new String[]{courseName}
                );
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // do nothing
            });
            builder.show();
            return true;
        });

        Button add_btn = findViewById(R.id.button);
        add_btn.setOnClickListener(v -> {
            final EditText txtUrl = new EditText(this);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add a course");
            builder.setMessage("Please enter the course name");
            builder.setView(txtUrl);
            builder.setPositiveButton("Add", (dialog, which) -> {
                String courseName = txtUrl.getText().toString();
                courseList.add(courseName);
                adapter.notifyDataSetChanged();
                ContentValues values = new ContentValues();
                values.put("courseName", courseName);
                resolver.insert(
                        Uri.parse("content://com.example.dbtest.CourseProvider/course"),
                        values
                );
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // do nothing
            });
            builder.show();
        });

        Button update_btn = findViewById(R.id.button1);
        update_btn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Update a course");
            builder.setView(R.layout.dialog_add_course);
            builder.setPositiveButton("Update", (dialog, which) -> {
                TextView oldCourseView = ((AlertDialog) dialog).findViewById(R.id.oldCourseName);
                TextView newCourseView = ((AlertDialog) dialog).findViewById(R.id.newCourseName);
                String oldCourseName = oldCourseView.getText().toString();
                String newCourseName = newCourseView.getText().toString();
                List<String> newCourseList = new ArrayList<>();
                for (String course : courseList) {
                    if (course.equals(oldCourseName)) {
                        newCourseList.add(newCourseName);
                    } else {
                        newCourseList.add(course);
                    }
                }
                courseList.clear();
                courseList.addAll(newCourseList);
                adapter.notifyDataSetChanged();
                ContentValues values = new ContentValues();
                values.put("courseName", newCourseName);
                resolver.update(
                        Uri.parse("content://com.example.dbtest.CourseProvider/course"),
                        values,
                        "courseName=?",
                        new String[]{oldCourseName}
                );
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                // do nothing
            });
            builder.show();
        });
    }
}
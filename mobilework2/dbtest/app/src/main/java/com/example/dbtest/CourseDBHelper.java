package com.example.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.List;

public class CourseDBHelper extends SQLiteOpenHelper {
    private static CourseDBHelper mInstance;
    public final static String TABLE_NAME = "course";
    private final static int DB_VERSION = 1;

    private CourseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized CourseDBHelper getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new CourseDBHelper(context, "name_list.db", null, DB_VERSION);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NAME_LIST_TABLE = "create table " + TABLE_NAME + " (" +
                "id integer primary key autoincrement, " +
                "courseName text)";
        db.execSQL(CREATE_NAME_LIST_TABLE);

        List<String> courses = Arrays.asList("物联网基础", "Android开发", "数据挖掘", "操作系统", "计算机网络", "操作系统", "数据结构", "C语言");
        for (String course : courses) {
            ContentValues values = new ContentValues();
            values.put("courseName", course);
            db.insert(TABLE_NAME, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
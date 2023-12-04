package com.example.dbtest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class CourseProvider extends ContentProvider {
    private final static String TAG = "CourseProvider";

    private SQLiteDatabase db = null;
    private final static int COURSE = 1;
    private final static int COURSES = 2;
    private final static String TABLE = CourseDBHelper.TABLE_NAME;

    private final static String AUTHORITY = "com.example.dbtest.CourseProvider";
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "course/#", COURSE);
        uriMatcher.addURI(AUTHORITY, "course", COURSES);
    }

    @Override
    public boolean onCreate() {
        db = CourseDBHelper.getInstance(getContext()).getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        int matchType = uriMatcher.match(uri);
        Cursor cursor = null;
        switch (matchType) {
            case COURSE:
                String id = uri.getPathSegments().get(1);
                cursor = db.query(TABLE, projection, "id=?", new String[]{id}, null, null, sortOrder);
                break;
            case COURSES:
                cursor = db.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        int matchType = uriMatcher.match(uri);
        switch (matchType) {
            case COURSE:
                return "vnd.android.cursor.item/vnd.com.example.dbtest.CourseProvider.course";
            case COURSES:
                return "vnd.android.cursor.dir/vnd.com.example.dbtest.CourseProvider.course";
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int matchType = uriMatcher.match(uri);
        switch (matchType) {
            case COURSE:
            case COURSES:
                long rowId = db.insert(TABLE, null, values);
                if (rowId == -1) {
                    Log.e(TAG, "insert error");
                    return null;
                }
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int matchType = uriMatcher.match(uri);
        int rowId = 0;
        switch (matchType) {
            case COURSE:
            case COURSES:
                rowId = db.delete(TABLE, selection, selectionArgs);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int matchType = uriMatcher.match(uri);
        int rowId = 0;
        switch (matchType) {
            case COURSE:
            case COURSES:
                rowId = db.update(TABLE, values, selection, selectionArgs);
                break;
            default:
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowId;
    }
}
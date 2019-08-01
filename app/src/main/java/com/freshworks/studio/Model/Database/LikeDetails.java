package com.freshworks.studio.Model.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// Building Data Master Table

public class LikeDetails {
    Context context;

    public static String TABLE_LIKEDETAILS = "LikeDetails";
    public static String URL = "url";
    public static String TITLE = "title";
    public static String GIF_ID = "gif_id";
    public static String LIKE = "is_like";


    private SQLiteDatabase database;
    private MySQLiteDatabse dbHelper;

    public LikeDetails(Context context) {
        dbHelper = new MySQLiteDatabse(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(ContentValues values) {
        database.insert(TABLE_LIKEDETAILS, null, values);
    }

    public boolean isEmpty() {
        Cursor cursor;
        cursor = database.query(TABLE_LIKEDETAILS,
                null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getCount() == 0;
    }

    public void removeFavourite(String gif_id) {
        database.delete(TABLE_LIKEDETAILS,
                GIF_ID + "='" + gif_id + "'",
                null);
    }

    public Cursor getData() {
        Cursor cursor = database.query(TABLE_LIKEDETAILS,
                null,
                null,
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();

        return cursor;
    }

    public boolean isLike(String gif_id) {
        Cursor cursor = database.query(TABLE_LIKEDETAILS,
                null,
                GIF_ID + "= '" + gif_id + "'",
                null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return true;
        } else
            return false;
    }
}

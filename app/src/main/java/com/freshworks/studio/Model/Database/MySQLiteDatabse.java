package com.freshworks.studio.Model.Database;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

// Create Database Class

public class MySQLiteDatabse extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MyDatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String ID = "_id";
    Context _context;

    public MySQLiteDatabse(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        _context = context;

    }

    public MySQLiteDatabse(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_LIKEDETAILS = String.format("CREATE TABLE IF NOT EXISTS %s " +
                        "(%s INTEGER PRIMARY KEY AUTOINCREMENT ," +
                        " %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                LikeDetails.TABLE_LIKEDETAILS,
                ID,
                LikeDetails.URL, LikeDetails.TITLE, LikeDetails.GIF_ID, LikeDetails.LIKE);

        database.execSQL(CREATE_LIKEDETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static boolean isDatabaseExist(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}

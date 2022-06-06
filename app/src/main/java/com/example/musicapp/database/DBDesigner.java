package com.example.musicapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBDesigner extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "music.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "favMusic";

    private static final String TITLE = "title";
    private static final String ARTIST = "artist";
    private static final String DURATION = "duration";
    private static final String PATH = "path";
    private static final String ALBUM = "album";

    public DBDesigner(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_fav_table = String.format("CREATE TABLE %s(%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, TITLE, ARTIST, ALBUM, DURATION, PATH);
        sqLiteDatabase.execSQL(create_fav_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String drop_fav_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        sqLiteDatabase.execSQL(drop_fav_table);
        onCreate(sqLiteDatabase);
    }
}

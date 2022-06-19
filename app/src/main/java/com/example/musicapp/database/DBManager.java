package com.example.musicapp.database;

import static com.example.musicapp.database.DBDesigner.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.Account;
import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class DBManager {
    private SQLiteDatabase database;
    private DBDesigner dbDesigner;

    public DBManager(Context context) {
        dbDesigner = new DBDesigner(context);
    }

    public void open() throws SQLException {
        database = dbDesigner.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void add (Song song) {
        ContentValues values = new ContentValues();
        values.put("title", song.getTitle());
        values.put("artist", song.getArtist());
        values.put("album", song.getAlbum());
        values.put("duration", song.getDuration());
        values.put("path", song.getPath());

        database.insert(TABLE_NAME, null, values);
    }

    public void deleteFav(String title) {
        database.delete(TABLE_NAME, "title = ?", new String[] { title });
    }

    public ArrayList<Song> getAll() {
        ArrayList<Song> favMusic = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM favMusic", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Song song = toSong(cursor);
            favMusic.add(song);
            cursor.moveToNext();
        }
        cursor.close();
        return favMusic;
    }

    public Account getAccount() {
        Cursor cursor = database.rawQuery("SELECT * FROM account WHERE id = 0", null);
        cursor.moveToFirst();
        Account account = new Account(cursor.getInt(0), cursor.getString(1), //id, username
                cursor.getString(2));                  // avatar
        cursor.close();
        return account;
    }

    public void changeUsernameAccount(String usernameAccount) {
        database.execSQL("UPDATE account " +
                "SET username = '" + usernameAccount + "' " +
                "WHERE id = 0");
    }

    public void changeAvatarAccount(String avatarAccount) {
        database.execSQL("UPDATE account " +
                "SET avatar = '" + avatarAccount + "' " +
                "WHERE id = 0");
    }

    private Song toSong(Cursor cursor) {
        Song song = new Song();
        song.setTitle(cursor.getString(0));
        song.setArtist(cursor.getString(1));
        song.setAlbum(cursor.getString(2));
        song.setDuration(cursor.getString(3));
        song.setPath(cursor.getString(4));

        return song;
    }
}

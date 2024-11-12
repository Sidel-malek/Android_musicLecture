package com.example.musi;

import android.content.ContentValues;
import android.content.Context;
import android.content.om.OverlayManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaBaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "musicDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_FAVORITES = "favorites";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_ARTIST = "artist";

    MaBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_PATH + " TEXT PRIMARY KEY," +
                COLUMN_ARTIST + " TEXT ," +
                COLUMN_TITLE + " TEXT );" ;
        sqLiteDatabase.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = " DROP TABLE IF EXISTS " + TABLE_FAVORITES + ";" ;
        db.execSQL(sql);
        onCreate(db);
    }

    public boolean addFavorite(String path, String title , String artist) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PATH, path);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_ARTIST,artist);

        return db.insert(TABLE_FAVORITES, null, values) != 0;

    }

    Cursor getFavorites() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(" SELECT * FROM " + TABLE_FAVORITES, null);
    }



    public Boolean removeFavorite(String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete( TABLE_FAVORITES,  COLUMN_PATH + "=?", new String[]{path}) !=0  ;
    }



    public boolean isFavorite(String path) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_PATH + "=?";
        Cursor cursor = db.rawQuery(sql, new String[]{path});
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getInt(0) > 0);
        }
        cursor.close();
        db.close();
        return exists;
    }


    public List<AudioModel> getAllFavorites() {
        List<AudioModel> favoriteList = new ArrayList<>();
        Cursor cursor = getFavorites();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ARTIST));
                AudioModel song = new AudioModel(path, title, artist );
                favoriteList.add(song);
            }
            cursor.close();
        }
        return favoriteList;
    }
}

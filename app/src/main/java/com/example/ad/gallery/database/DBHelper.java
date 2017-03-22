package com.example.ad.gallery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongbonba on 3/20/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    private SQLiteDatabase db;
    public static final String DB_NAME = "Gallery.db";
    public static final String FAVORITE_TABLE_NAME = "favorite";
    public static final String FAVORITE_COLUMN_PATH = "path";
    public static final String FAVORITE_COLUMN_ISLIKED = "isLiked";
    public static final int DB_VERSION = 3;

    public DBHelper(Context context, String DB_NAME, SQLiteDatabase.CursorFactory factory, int DB_VERSION) {
        super(context, DB_NAME, factory, DB_VERSION);
        this.db = getWritableDatabase();
    }

    public DBHelper(Context context, String DB_NAME, SQLiteDatabase.CursorFactory factory, int DB_VERSION, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, factory, DB_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE `favorite` (\n" +
                "`path` varchar(255) PRIMARY KEY\n" +
                "`isLiked` int,\n" +
                ");";
        Log.v("SQLite01", sql);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS favorite");
        onCreate(db);
    }

    //return true if insert success
    public boolean insertNewRecord(String path) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(FAVORITE_COLUMN_PATH, path);
            cv.put(FAVORITE_COLUMN_ISLIKED, true);

            db.insert(FAVORITE_TABLE_NAME, null, cv);
            Log.v("InsertNewRecord", "Completed!");
            db.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FavoriteImage> getAllFavoriteImage() {
        List<FavoriteImage> list = new ArrayList<>();
        Cursor rs = null;
        String sql = "SELECT path FROM favorite";
        rs = this.db.rawQuery(sql, null);
        Log.v("NumberOfSelectedRecords", "Number of selected record(s): " + rs.getCount());
        rs.moveToFirst();
        while(rs.isAfterLast() == false) {
            int like = rs.getInt(rs.getColumnIndex(FAVORITE_COLUMN_ISLIKED));
            if (like == 0) {
                continue;
            }
            FavoriteImage fi = new FavoriteImage();
            fi.setPath(rs.getString(rs.getColumnIndex(FAVORITE_COLUMN_PATH)));
            fi.setLiked(true);
            list.add(fi);
            rs.moveToNext();
        }
        rs.close();
        return list;
    }

    public List<FavoriteImage> getAllUnlikeImage() {
        List<FavoriteImage> list = new ArrayList<>();
        Cursor rs = null;
        String sql = "SELECT path FROM favorite";
        rs = this.db.rawQuery(sql, null);
        Log.v("NumberOfSelectedRecords", "Number of selected record(s): " + rs.getCount());
        rs.moveToFirst();
        while(rs.isAfterLast() == false) {
            int like = rs.getInt(rs.getColumnIndex(FAVORITE_COLUMN_ISLIKED));
            if (like != 0) {
                continue;
            }
            FavoriteImage fi = new FavoriteImage();
            fi.setPath(rs.getString(rs.getColumnIndex(FAVORITE_COLUMN_PATH)));
            fi.setLiked(false);
            list.add(fi);
            rs.moveToNext();
        }
        rs.close();
        return list;
    }

    public boolean updateRecord(FavoriteImage fi) {
        boolean result = false; //if we can update successfully the passed contact => return true;
        ContentValues cv = new ContentValues();
        cv.put(FAVORITE_COLUMN_PATH, fi.getPath());
        cv.put(FAVORITE_COLUMN_ISLIKED, fi.isLiked());

        String whereClause = FAVORITE_COLUMN_PATH;
        String[] whereValues = {fi.getPath() + ""};
        int count = this.db.update(FAVORITE_TABLE_NAME, cv, whereClause, whereValues);
        if (count == 0) {
            Log.v("UpdateRecord", "Nothing is updated.");
        }
        else {
            Log.v("UpdateRecord", count + " record(s) is/ are updated.");
            result = true;
        }
        return result;
    }
}

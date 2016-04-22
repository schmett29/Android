package com.example.david.todohw4partb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by David on 3/28/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public String TABLE_NAME = "initial";
    public static final String COLUMN_ID = "_id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";

    private static final String DATABASE_NAME = "commments.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    public String DATABASE_CREATE = "create table if not exists "
            + this.TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + TITLE
            + " VARCHAR(250), " + DESCRIPTION
            + " VARCHAR(250), " + DATE
            + " VARCHAR(250));";

    public MySQLiteHelper(Context context, String name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.TABLE_NAME = name;

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public void create(SQLiteDatabase database, String Name) {
        database.execSQL("create table if not exists "
                + Name + "(" + COLUMN_ID
                + " integer primary key autoincrement, " + TITLE
                + " VARCHAR(250), " + DESCRIPTION
                + " VARCHAR(250), " + DATE
                + " VARCHAR(250));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getTableName(){
        return this.TABLE_NAME;
    }

    public void setTableName(String name){
        this.TABLE_NAME = name;
    }
}

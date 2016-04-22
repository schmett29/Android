package com.example.david.hw5partb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 4/9/16.
 */
public class citiesDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.NAME, MySQLiteHelper.POP1, MySQLiteHelper.POP2, MySQLiteHelper.DIFF };

    private static final String TAG = "DBDEMO";

    public citiesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public cities createCity(String name, int pop1, int pop2, int diff) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.NAME, name);
        values.put(MySQLiteHelper.POP1, pop1);
        values.put(MySQLiteHelper.POP2, pop2);
        values.put(MySQLiteHelper.DIFF, diff);
        long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cities newCity = cursorToComment(cursor);

        // Log the comment stored
        Log.d(TAG, "comment = " + cursorToComment(cursor).toString()
                + " insert ID = " + insertId);

        cursor.close();
        return newCity;
    }

    public void deleteComment(cities city) {
        long id = city.getID();
        Log.d(TAG, "delete comment = " + id);
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllComments() {
        System.out.println("Comment deleted all");
        Log.d(TAG, "delete all = ");
        database.delete(MySQLiteHelper.TABLE_NAME, null, null);
    }

    public void updateCity(int id, String name, int pop1, int pop2, int diff){
        ContentValues newValues = new ContentValues();
        newValues.put(MySQLiteHelper.NAME, name);
        newValues.put(MySQLiteHelper.POP1, pop1);
        newValues.put(MySQLiteHelper.POP2, pop2);
        newValues.put(MySQLiteHelper.DIFF, diff);
        database.update(MySQLiteHelper.TABLE_NAME, newValues, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public cities increase(){
        Cursor cursor = database.rawQuery("SELECT * FROM "
                + MySQLiteHelper.TABLE_NAME
                + " WHERE "
                + MySQLiteHelper.DIFF
                + " = (SELECT MAX("
                + MySQLiteHelper.DIFF
                + ") FROM "
                + MySQLiteHelper.TABLE_NAME
                + ");"
                ,null);
        cursor.moveToFirst();
        cities city = cursorToComment(cursor);
        cursor.close();
        return city;

    }
    public cities decrease(){
        Cursor cursor = database.rawQuery("SELECT * FROM "
                        + MySQLiteHelper.TABLE_NAME
                        + " WHERE "
                        + MySQLiteHelper.DIFF
                        + " = (SELECT MIN("
                        + MySQLiteHelper.DIFF
                        + ") FROM "
                        + MySQLiteHelper.TABLE_NAME
                        + ");"
                ,null);
        cursor.moveToFirst();
        cities city = cursorToComment(cursor);
        cursor.close();
        return city;

    }
    public ArrayList<cities> citylist(){
        ArrayList<cities> thecities = new ArrayList<cities>();
        Cursor cursor = database.rawQuery("SELECT * FROM "
                        + MySQLiteHelper.TABLE_NAME
                        + " WHERE "
                        + MySQLiteHelper.POP2
                        + " < 5000;"
                ,null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cities city = cursorToComment(cursor);
            Log.d(TAG, "get comment = " + cursorToComment(cursor).toString());
            thecities.add(city);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return thecities;

    }

    public ArrayList<cities> getAllComments() {
        ArrayList<cities> thecities = new ArrayList<cities>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            cities city = cursorToComment(cursor);
            Log.d(TAG, "get comment = " + cursorToComment(cursor).toString());
            thecities.add(city);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return thecities;
    }

    private cities cursorToComment(Cursor cursor) {
        cities city = new cities("","","");
        city.setID(cursor.getInt(0));
        city.setName(cursor.getString(1));
        city.setPop1(cursor.getString(2));
        city.setPop2(cursor.getString(3));
        city.setDiff(cursor.getInt(4));
        return city;
    }
}

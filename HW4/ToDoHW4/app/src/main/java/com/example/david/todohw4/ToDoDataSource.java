package com.example.david.todohw4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 3/20/16.
 */
public class ToDoDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.TITLE, MySQLiteHelper.DESCRIPTION, MySQLiteHelper.DATE };

    private static final String TAG = "DBDEMO";

    public ToDoDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public ToDo createToDo(String title, String description, String date) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TITLE, title);
        values.put(MySQLiteHelper.DESCRIPTION, description);
        values.put(MySQLiteHelper.DATE, date);
        long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ToDo newComment = cursorToComment(cursor);

        // Log the comment stored
        Log.d(TAG, "comment = " + cursorToComment(cursor).toString()
                + " insert ID = " + insertId);

        cursor.close();
        return newComment;
    }

    public void deleteComment(ToDo todo) {
        int id = todo.getID();
        Log.d(TAG, "delete comment = " + id);
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateComment(int id, String title, String description){
        ContentValues newValues = new ContentValues();
        newValues.put(MySQLiteHelper.TITLE, title);
        newValues.put(MySQLiteHelper.DESCRIPTION, description);
        database.update(MySQLiteHelper.TABLE_NAME, newValues, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public void deleteAllComments() {
        System.out.println("Comment deleted all");
        Log.d(TAG, "delete all = ");
        database.delete(MySQLiteHelper.TABLE_NAME, null, null);
    }

    public List<ToDo> getAllComments() {
        List<ToDo> todos = new ArrayList<ToDo>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ToDo todo = cursorToComment(cursor);
            Log.d(TAG, "get comment = " + cursorToComment(cursor).toString());
            todos.add(todo);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return todos;
    }

    private ToDo cursorToComment(Cursor cursor) {
        ToDo todo = new ToDo("","","", false);
        todo.setID(cursor.getInt(0));
        todo.setTitle(cursor.getString(1));
        todo.setDescription(cursor.getString(2));
        todo.setDatecreated(cursor.getString(3));
        return todo;
    }
}

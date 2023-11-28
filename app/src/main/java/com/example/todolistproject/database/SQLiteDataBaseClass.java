package com.example.todolistproject.database;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todolistproject.model.TaskModel;

import java.util.ArrayList;
import java.util.List;


public class SQLiteDataBaseClass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dataBase";
    private static final String TABLE_TASK = "tableTask";

    public SQLiteDataBaseClass(@Nullable Context context) {
        super(context,DATABASE_NAME, null, 1);
    }
;
    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            //create my table and columns
            db.execSQL("CREATE TABLE "+TABLE_TASK+" (id INTEGER PRIMARY KEY, title TEXT, completed BOOLEAN)");
        }catch (SQLException e){
            Log.e("TAG", "onCreate: "+e.toString() );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long addTask(TaskModel taskModel){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",taskModel.getTitle());
        values.put("completed",taskModel.isCompleted());
        long result = sqLiteDatabase.insert(TABLE_TASK,null,values);
        sqLiteDatabase.close();
        return result;
    }
    public List<TaskModel> getTask(){
        List<TaskModel> taskModel = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TASK,null);
        if (cursor.moveToFirst()){
            do {
                TaskModel task = new TaskModel();
                task.setId(cursor.getLong(0));
                task.setTitle(cursor.getString(1));
                task.setCompleted(cursor.getInt(2)==1);
                taskModel.add(task);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return taskModel;
    }
    public int deleteTask(TaskModel task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int result = sqLiteDatabase.delete(TABLE_TASK,"id = ?",new String[]{String.valueOf(task.getId())});
        sqLiteDatabase.close();
        return result;
    }
    public void clearAllData(){
        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.execSQL("DELETE FROM "+TABLE_TASK);
        }catch (SQLException e){
            Log.e(TAG, "clearAllData: "+e.toString() );
        }
    }
    public int updateTask(TaskModel taskModel){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",taskModel.getTitle());
        values.put("completed",taskModel.isCompleted());
        int result = sqLiteDatabase.update(TABLE_TASK,values,"id = ?",new String[]{String.valueOf(taskModel.getId())});
        sqLiteDatabase.close();
        return result;
    }
    public List<TaskModel> searchBox(String query){
        List<TaskModel> taskModel = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_TASK+" WHERE title LIKE '%"+query+"%'",null);
        if (cursor.moveToFirst()){
            do {
                TaskModel task = new TaskModel();
                task.setId(cursor.getLong(0));
                task.setTitle(cursor.getString(1));
                task.setCompleted(cursor.getInt(2)==1);
                taskModel.add(task);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return taskModel;
    }
}

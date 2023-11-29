package com.example.todolistproject.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todolistproject.interface_dao.TaskDao;
import com.example.todolistproject.model.TaskModel;

@Database(version = 1,exportSchema = false,entities = {TaskModel.class})
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase;

    public static AppDataBase getAppDataBase(Context context) {
        //Don't create an object every time
        if (appDataBase == null){
            appDataBase = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class,"dataBase")
                    //Permission to run on the main thread
                    .allowMainThreadQueries().build();
        }
        return appDataBase;
    }
    //for conection
    public abstract TaskDao getTaskDao();
}

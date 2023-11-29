package com.example.todolistproject.interface_dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolistproject.model.TaskModel;

import java.util.List;

@Dao //data access object
public interface TaskDao {
    @Insert
    long addTask(TaskModel task);

    @Update
    int updateTask(TaskModel task);

    @Delete
    int deleteTask(TaskModel taskModel);

    @Query("SELECT * FROM tableTask")
    List<TaskModel> getTasks();

    @Query("SELECT * FROM tableTask WHERE title LIKE '%' || :query || '%'")
    List<TaskModel> search(String query);

    @Query("DELETE FROM tableTask")
    void clearAll();
    
}

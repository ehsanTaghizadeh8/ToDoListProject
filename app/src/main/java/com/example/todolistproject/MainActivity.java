package com.example.todolistproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.todolistproject.adapter.TaskAdapter;
import com.example.todolistproject.database.AppDataBase;
import com.example.todolistproject.dialogfragment.AddTaskDialog;
import com.example.todolistproject.dialogfragment.EditTaskDialog;
import com.example.todolistproject.interface_dao.TaskDao;
import com.example.todolistproject.model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.addNewTaskCallBack, TaskAdapter.taskItemEventListener
, EditTaskDialog.editTaskCallBack {
 RecyclerView recyclerView;
 FloatingActionButton btnAdd;
 AppCompatImageView clearAll;
 SearchView searchBox;
 List<TaskModel> taskModels;
 TaskAdapter adapter = new TaskAdapter(this);
private TaskDao taskDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize taskDao interface
        taskDao = AppDataBase.getAppDataBase(this).getTaskDao();
        //find by id
        searchBox = findViewById(R.id.searchView);
        clearAll = findViewById(R.id.btn_clear_all);
        btnAdd = findViewById(R.id.btn_main_add);
        recyclerView = findViewById(R.id.recycler_main);
        //get tasks
        taskModels = taskDao.getTasks();
        //initialize list of adapter
        adapter.addItems(taskModels);
        //recycler set options
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        //set searchView
        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    List<TaskModel> task = taskDao.search(newText);
                    adapter.setTask(task);
                } else {
                    List<TaskModel> task = taskDao.getTasks();
                    adapter.setTask(task);
                }
                return true;
            }
        });
        //set button to add new tasks
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create fragment dialog to add new task in main
                AddTaskDialog addTaskDialog = new AddTaskDialog();
                addTaskDialog.show(getSupportFragmentManager(),null);
            }
        });
        //set button for clear all tasks
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear all task in my list and adapter
                adapter.clearAllItems();
                //clear all data in sqlite data base
                taskDao.clearAll();
            }
        });
    }

    @Override
    public void clickToAdd(TaskModel task) {
        //get id
        long id = taskDao.addTask(task);
        if (id != -1){
            task.setId(id);
            //add items in adapter in zero position
            adapter.addTask(task);
        }else
            Log.e("TAG", "clickToAdd: "+"error in add task" );

    }
    @Override
    public void onDeleteButtonPress(TaskModel task) {
        //if id bigger than zero call remove method in adapter and remove item in adapter
        int id = taskDao.deleteTask(task);
        if (id > 0){
            adapter.removeItem(task);
        }
    }

    @Override
    public void onUpdateTaskLongPress(TaskModel task) {
        //set long press to edit task
        EditTaskDialog editTaskDialog = new EditTaskDialog();
        Bundle bundle = new Bundle();
        //put data in bundle
        bundle.putParcelable("task",task);
        editTaskDialog.setArguments(bundle);
        editTaskDialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onItemCheckedChange(TaskModel taskModel) {
        //set check change check box
        taskDao.updateTask(taskModel);
    }

    @Override
    public void clickToEdit(TaskModel task) {
        //interface from editTaskDialog to update item
        int result = taskDao.updateTask(task);
        if (result > 0){
            adapter.updateItem(task);
        }
    }
}
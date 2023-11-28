package com.example.todolistproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistproject.R;
import com.example.todolistproject.dialogfragment.AddTaskDialog;
import com.example.todolistproject.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    List<TaskModel> taskModel = new ArrayList<>();
    taskItemEventListener listener;

    public TaskAdapter(taskItemEventListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position) {
        holder.bindTask(taskModel.get(position));
    }
    public void addTask(TaskModel task){
        //add new item in zero position
        taskModel.add(0,task);
        notifyItemInserted(0);
    }
    public void addItems(List<TaskModel> taskModel){
        //add all items in recyclerView
        this.taskModel.addAll(taskModel);
        notifyDataSetChanged();
    }
    public void clearAllItems(){
        //clear all items in recyclerView
        this.taskModel.clear();
        notifyDataSetChanged();
    }
    public void removeItem(TaskModel task){
        //in the first stage of ID checking,if it is correct the desired item will be deleted and the ring will be completed
        for (int i = 0; i < taskModel.size(); i++) {
            if (taskModel.get(i).getId()==task.getId()){
                taskModel.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
    public void updateItem(TaskModel task){
        //in the first stage of checking the ID, if the desired item is correct, it will be edited and the loop will be completed
        for (int i = 0; i <taskModel.size() ; i++) {
            if (taskModel.get(i).getId()==task.getId()){
               taskModel.set(i,task);
               notifyItemChanged(i);
               break;
            }
        }
    }
    public void setTask(List<TaskModel> task) {
        this.taskModel = task;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskModel.size();
    }
    public class TaskHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox checkBox;
        AppCompatImageView btnDelete;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox_task_itemList);
            btnDelete = itemView.findViewById(R.id.btn_delete_task);

        }
        public void bindTask(TaskModel task){
            checkBox.setOnCheckedChangeListener(null);
            //set title and check box when binding
           checkBox.setText(task.getTitle());
           checkBox.setChecked(task.isCompleted());
           //when you click on the trash,the desired item will be deleted
           btnDelete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   //callback in main activity
                  listener.onDeleteButtonPress(task);
               }
           });
           //set long press item to edit
           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   listener.onUpdateTaskLongPress(task);
                   return true;
               }
           });
           //set checked change checkBox
           checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   task.setCompleted(isChecked);
                   listener.onItemCheckedChange(task);

               }
           });
        }
    }
    public interface taskItemEventListener {
        void onDeleteButtonPress(TaskModel task);
        void onUpdateTaskLongPress(TaskModel task);
        void onItemCheckedChange(TaskModel taskModel);
    }
}

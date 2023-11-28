package com.example.todolistproject.dialogfragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.todolistproject.R;
import com.example.todolistproject.model.TaskModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddTaskDialog extends DialogFragment {
    addNewTaskCallBack addNewTaskCallBack;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //create view for my add item list fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_list_item,null,false);
        //find by id
        TextInputLayout inputLayout = view.findViewById(R.id.inputLayout_addTaskDialog_addTitle);
        TextInputEditText editText = view.findViewById(R.id.editTxt_addTaskDialog_addTitle);
        MaterialButton btnAdd = view.findViewById(R.id.btn_add_dialog);
        //set on clickListener for add task to main
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.length()>0){
                    //create taskModel and set title and checkbox in main
                    TaskModel taskModel = new TaskModel();
                    taskModel.setTitle(editText.getText().toString());
                    taskModel.setCompleted(false);
                    //put interface
                    addNewTaskCallBack.clickToAdd(taskModel);
                    //when the operation is done, the screen disappears
                    dismiss();
                }else {
                    //set error when editText is empty
                    inputLayout.setError("please enter the title");
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }
    public interface addNewTaskCallBack {
        void clickToAdd(TaskModel task);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        addNewTaskCallBack = (addNewTaskCallBack) context;
    }
}

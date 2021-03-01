package com.example.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ToDoAdapter;
import com.example.myapplication.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class MainTasks extends AppCompatActivity {

    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;

    private List<ToDoModel> taskList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textToolbar)).setText("Tasks");


        taskList = new ArrayList<>();

        tasksRecyclerView = findViewById(R.id.tasksRecycle);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("This is a Test Task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);        taskList.add(task);
        taskList.add(task);        taskList.add(task);
        taskList.add(task);        taskList.add(task);
        taskList.add(task);

        tasksAdapter.setTasks(taskList);
    }
}

package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ToDoAdapter;
import com.example.myapplication.Model.ToDoModel;

import java.util.ArrayList;

public class ShoppingListView extends AppCompatActivity {

    private String listName;
    private ArrayList<ToDoModel> items;
    private RecyclerView itemsRecyclerView;
    private ToDoAdapter itemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_shoppinglist);

        listName = getIntent().getStringExtra("ListName");
        items = (ArrayList<ToDoModel>) getIntent().getSerializableExtra("ListItems");

        ((TextView)findViewById(R.id.textToolbar)).setText(listName);

        itemsRecyclerView = findViewById(R.id.tasksRecycle);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ToDoAdapter(this);
        itemsRecyclerView.setAdapter(itemAdapter);

        ToDoModel task = new ToDoModel();
        task.setTask("New ITEM");
        task.setStatus(0);
        task.setId(1);

        items.add(task);


        itemAdapter.setTasks(items);

    }
}

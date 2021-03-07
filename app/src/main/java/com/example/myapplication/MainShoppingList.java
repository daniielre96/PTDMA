package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ShoppingAdapter;
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class MainShoppingList extends AppCompatActivity {

    private RecyclerView shoppingRecyclerView;
    private ShoppingAdapter shoppingAdapter;
    
    private List<ShoppingModel> shoppingList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textToolbar)).setText("Shopping List");

        final ImageButton microButton = findViewById(R.id.fab);

        microButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainShoppingList.this, CreateShoppingList.class);
                startActivity(intent);
            }
        });
        
        shoppingList = new ArrayList<>();
        
        shoppingRecyclerView = findViewById(R.id.tasksRecycle);
        shoppingRecyclerView.setLayoutManager(new GridLayoutManager(MainShoppingList.this, 2, GridLayoutManager.VERTICAL, false));
        shoppingAdapter = new ShoppingAdapter(this);
        shoppingRecyclerView.setAdapter(shoppingAdapter);

        ShoppingModel model = new ShoppingModel();
        model.setId(1);
        model.setTitle("List 1");
        model.setImage(R.drawable.ic_baseline_shopping_cart_24);

        // Items from shopping List

        ArrayList<ToDoModel> items = new ArrayList<>();
        ToDoModel task = new ToDoModel();
        task.setTask("Item 1");
        task.setStatus(0);
        task.setId(1);

        items.add(task);
        items.add(task);
        items.add(task);
        items.add(task);        items.add(task);
        items.add(task);        items.add(task);
        items.add(task);        items.add(task);
        items.add(task);

        model.setItems(items);

        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);
        shoppingList.add(model);

        shoppingAdapter.setShoppingList(shoppingList);

    }
}

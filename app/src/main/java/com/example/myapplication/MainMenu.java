package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        final RelativeLayout toDoButton = findViewById(R.id.mainMenuToDoButton);
        final RelativeLayout eventsButton = findViewById(R.id.mainMenuEventsButton);
        final RelativeLayout shoppingButton = findViewById(R.id.mainMenuShoppingButton);

        /* Button to To-Do List */
        toDoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MainTasks.class);
                startActivity(intent);
            }
        });

        /* Button to Events List */
        eventsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, MainEvents.class);
                startActivity(intent);
            }
        });

        /* Button to Shopping List */
    }
}
package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.EventAdapter;
import com.example.myapplication.Model.EventModel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainEvents extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private EventAdapter eventsAdapter;

    private List<EventModel> eventList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.textToolbar)).setText("Events");

        final ImageButton microButton = findViewById(R.id.fab);

        microButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainEvents.this, CreateEvent.class);
                startActivity(intent);
            }
        });

        eventList = new ArrayList<>();

        eventsRecyclerView = findViewById(R.id.tasksRecycle);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventAdapter(this);
        eventsRecyclerView.setAdapter(eventsAdapter);

        // Insertion of actual events

        int k = 1;
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM");

        for(int i=0; i < 5; i++){

            EventModel event = new EventModel();
            try {
                event.setDate(dateformat.parse("12/07"));

                event.setEvent("Event 1");
                event.setStatus(0);
                event.setId(k);
                eventList.add(event);
                k++;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Insertion of past events

        for(int i=0; i < 5; i++){

            EventModel event = new EventModel();
            try {
                event.setDate(dateformat.parse("01/01"));

                event.setEvent("Passed Event");
                event.setStatus(0);
                event.setId(k);
                eventList.add(event);
                k++;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        eventsAdapter.setEvents(eventList);
    }
}

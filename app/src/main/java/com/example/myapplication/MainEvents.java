package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
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

        eventList = new ArrayList<>();

        eventsRecyclerView = findViewById(R.id.tasksRecycle);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventAdapter(this);
        eventsRecyclerView.setAdapter(eventsAdapter);

        EventModel event = new EventModel();
        EventModel eventPassed = new EventModel();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM");
        try {
            event.setDate(dateformat.parse("12/07"));

            event.setEvent("Event 1");
            event.setStatus(0);
            event.setId(1);

            eventPassed.setDate(dateformat.parse("01/01"));
            eventPassed.setEvent("Passed Event");
            eventPassed.setStatus(0);
            eventPassed.setId(1);

            eventList.add(eventPassed);
            eventList.add(eventPassed);
            eventList.add(eventPassed);
            eventList.add(eventPassed);
            eventList.add(eventPassed);


            eventList.add(event);
            eventList.add(event);
            eventList.add(event);
            eventList.add(event);        eventList.add(event);
            eventList.add(event);        eventList.add(event);
            eventList.add(event);        eventList.add(event);
            eventList.add(event);

            eventsAdapter.setEvents(eventList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

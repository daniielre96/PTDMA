package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.EventAdapter;
import com.example.myapplication.Model.EventModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QueryEvents extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private EventAdapter eventsAdapter;

    private List<EventModel> eventList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_view);
        ((TextView)findViewById(R.id.textToolbar)).setText("Events query");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_filter);

        eventList = new ArrayList<>();

        eventsRecyclerView = findViewById(R.id.tasksRecycle);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventAdapter(this);
        eventsRecyclerView.setAdapter(eventsAdapter);

        EventModel event = new EventModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

        try{
            event.setDate(dateFormat.parse("25/12"));
            event.setEvent("Merry Christmas");
            event.setStatus(0);
            event.setId(1);

            eventList.add(event);
            eventList.add(event);
            eventList.add(event);
            eventList.add(event);
            eventList.add(event);

            eventsAdapter.setEvents(eventList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

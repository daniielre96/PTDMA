package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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

public class MainEvents extends Fragment {

    private RecyclerView eventsRecyclerView;
    private EventAdapter eventsAdapter;

    private List<EventModel> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((TextView)getActivity().findViewById(R.id.textToolbar)).setText("Events");

        final ImageButton microButton = getView().findViewById(R.id.fab);

        microButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CreateEvent.class);
                startActivity(intent);
            }
        });

        eventList = new ArrayList<>();

        eventsRecyclerView = getView().findViewById(R.id.tasksRecycle);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsAdapter = new EventAdapter(getContext());
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

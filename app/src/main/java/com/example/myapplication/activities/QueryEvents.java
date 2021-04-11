package com.example.myapplication.activities;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.EventAdapter;
import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.Model.EventModel;
import com.example.myapplication.R;
import com.example.myapplication.comandVoice.Voice;

import java.text.ParseException;
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

        Voice.instancia().speak(getString(R.string.Query), TextToSpeech.QUEUE_FLUSH, null, "text");

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
                event.setDate(dateformat.parse("25/12"));

                event.setEvent("Merry Christmas");
                event.setStatus(0);
                //event.setId(k);
                eventList.add(event);
                k++;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        eventsAdapter.setEvents(eventList);
    }
}

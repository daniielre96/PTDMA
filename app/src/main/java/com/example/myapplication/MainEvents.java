package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.EventAdapter;
import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.EventModel;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.Voice;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainEvents extends Listen {

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

        if(!((GlobalVars)this.getActivity().getApplication()).isMainEventsWelcome()) Voice.instancia().speak(getString(R.string.MainEventsWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getActivity().getApplication()).setMainEventsWelcome(true);

        ((TextView)getActivity().findViewById(R.id.textToolbar)).setText("Events");

        final ImageButton microButton = getView().findViewById(R.id.fab);

        /** SPEECH RECONIZER **/

        microButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    microButton.setImageResource(R.drawable.ic_grupo_48);
                    ((Listen)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container)).stopListening();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    microButton.setImageResource(R.drawable.ic_grupo_48_red);
                    ((Listen)getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container)).startListening();
                }

                return false;
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

    @Override
    public void getResult(String result) {

        int action = Message.parseMainEvents(result);

        switch (action){
            case 0: // UNDEFINED COMMAND
                break;
            case 1: // HELP
                break;
            case 2: //  DELETE EVENT
                break;
            case 3: // DELETE ALL EVENTS FROM A DAY
                break;
            case 4: // CREATE AN EVENT
                break;
            case 5: // MODIFY AN EVENT
                break;
            case 6: // ENABLE SOUND
                break;
            case 7: // DISABLE SOUND
                break;
        }
    }
}

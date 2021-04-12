package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.CalendarContract;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.CreateEvent;
import com.example.myapplication.activities.CreateTask;
import com.example.myapplication.comandVoice.Listen;
import com.example.myapplication.comandVoice.Voice;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainEvents extends Listen {

    private RecyclerView eventsRecyclerView;
    private EventAdapter eventsAdapter;
    private Dialog dialog;
    private ImageButton helpButton;
    private BottomNavigationView bottomNav;

    private List<EventModel> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        eventList = EventModel.listAll(EventModel.class);

        eventsAdapter.setEvents(eventList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(!((GlobalVars)this.getActivity().getApplication()).isMainEventsWelcome()) Voice.instancia().speak(getString(R.string.MainEventsWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");
        ((GlobalVars)this.getActivity().getApplication()).setMainEventsWelcome(true);

        ((TextView)getActivity().findViewById(R.id.textToolbar)).setText("Events");

        final ImageButton microButton = getView().findViewById(R.id.fab);

        helpButton = getActivity().findViewById(R.id.toolbarRightIcon);
        dialog = new Dialog(this.getContext());

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

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
    }

    @Override
    public void getResult(String result) {

        int action = Message.parseMainEvents(result);

        switch (action){
            case 0: // UNDEFINED COMMAND (DONE)
                undefinedCommand();
                break;
            case 1: // HELP (DONE)
                openDialog();
                break;
            case 2: //  DELETE EVENT (DONE)
                deleteEvent(result);
                break;
            case 3: // DELETE ALL EVENTS FROM A DAY
                break;
            case 4: // CREATE AN EVENT (DONE)
                createEvent();
                break;
            case 5: // MODIFY AN EVENT
                modifyEvent(result);
                break;
            case 6: // ENABLE SOUND (DONE)
                GlobalVars.setNotificationsEnable(true);
                break;
            case 7: // DISABLE SOUND (DONE)
                GlobalVars.setNotificationsEnable(false);
                break;
            case 8: // GO TO TO DO LIST (DONE)
                bottomNav = getActivity().findViewById(R.id.navbar);
                bottomNav.setSelectedItemId(R.id.todo_list);
                break;
            case 9: // GO TO SHOPPING LIST (DONE)
                bottomNav = getActivity().findViewById(R.id.navbar);
                bottomNav.setSelectedItemId(R.id.shopping_list);
                break;
        }
    }

    /* COMMANDS ACTIONS METHODS */

    private void undefinedCommand() {
        Voice.instancia().speak(getString(R.string.UndefinedCommand), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void openDialog() {
        dialog.setContentView(R.layout.help_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();

        Voice.instancia().speak(getString(R.string.HelpMe), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void createEvent() {
        Intent myIntent = new Intent(this.getActivity(), CreateEvent.class);
        startActivity(myIntent);
    }

    private void deleteEvent(String result) {
        String id = Message.getAfterString("event ", result);
        int realId = GlobalVars.idWordToInt(id, eventList);

        if(realId != 0) {

            EventModel model = EventModel.findById(EventModel.class, realId);
            long eventId = model.getEventId();

            ContentResolver cr = getActivity().getContentResolver();
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId);
            cr.delete(deleteUri, null, null);
            model.delete();
            eventList = EventModel.listAll(EventModel.class);
            eventsAdapter.setEvents(eventList);
        }
        else{
            Voice.instancia().speak("Event not found", TextToSpeech.QUEUE_FLUSH, null, "text");
        }

    }

    private void modifyEvent(String result){
        String id = Message.getAfterString("event ", result);
        int realId = GlobalVars.idWordToInt(id, eventList);

        if(realId != 0) {

            EventModel model = EventModel.findById(EventModel.class, realId);
            long eventId = model.getEventId();

            Intent intent = new Intent(this.getActivity(), CreateEvent.class);
            intent.putExtra("EventModel", model);
            startActivity(intent);
        }
        else{
            Voice.instancia().speak("Event not found", TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }
}

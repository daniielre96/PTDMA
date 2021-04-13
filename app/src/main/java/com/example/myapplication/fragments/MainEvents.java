package com.example.myapplication.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.DateFormat;
import java.util.Calendar;
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
    long calId;

    private List<EventModel> eventList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        GetCaliD();
        showEvents();

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
            case 5: // MODIFY AN EVENT (DONE)
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

    private void GetCaliD(){
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID, // 0
                CalendarContract.Calendars.ACCOUNT_NAME, // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME // 2
        };

// The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;


// Run query
        Cursor cur = null;
        ContentResolver cr = getActivity().getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "";
        String[] selectionArgs = new String[] {};
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

// Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);


// Do something with the values...
            ((GlobalVars)this.getActivity().getApplication()).setIdCal(calID);
        }
    }

    private void showEvents() {
        final String[] INSTANCE_PROJECTION = new String[] {
                CalendarContract.Instances.EVENT_ID,       // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE,        // 2
                CalendarContract.Instances.ORGANIZER    //3
        };

        // The indices for the projection array above.
        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_BEGIN_INDEX = 1;
        final int PROJECTION_TITLE_INDEX = 2;
        final int PROJECTION_ORGANIZER_INDEX = 3;

        // Specify the date range you want to search for recurring event instances
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Calendar.YEAR, 2021);
        beginTime.set(Calendar.MONTH, 3);
        beginTime.set(Calendar.DATE, 12);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.YEAR, 2021);
        endTime.set(Calendar.MONTH, 4);
        endTime.set(Calendar.DATE, 12);
        long endMillis = endTime.getTimeInMillis();


        // The ID of the recurring event whose instances you are searching for in the Instances table
        String selection = "";
        String[] selectionArgs = new String[] {};

        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);

        // Submit the query
        Cursor cur =  getActivity().getContentResolver().query(builder.build(), INSTANCE_PROJECTION, selection, selectionArgs, null);


        ArrayList<String> events = new ArrayList<>();
        while (cur.moveToNext()) {
            // Get the field values
            long eventID = cur.getLong(PROJECTION_ID_INDEX);
            long beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
            String title = cur.getString(PROJECTION_TITLE_INDEX);
            String organizer = cur.getString(PROJECTION_ORGANIZER_INDEX);

            // Do something with the values.
            //Log.i("Calendar", "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            //Log.i("Calendar", "Date: " + formatter.format(calendar.getTime()));

            events.add(String.format("Event: %s\nOrganizer: %s\nDate: %s", title, organizer, formatter.format(calendar.getTime())));
        }
    }
}

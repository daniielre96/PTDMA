package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Global.GlobalVars;
import com.example.myapplication.MessageParser.Message;
import com.example.myapplication.Model.EventModel;
import com.example.myapplication.R;
import com.example.myapplication.comandVoice.ListenActivity;
import com.example.myapplication.comandVoice.Voice;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CreateEvent extends ListenActivity {

    private ImageButton helpButton;
    private Dialog dialog;
    private EventModel model;
    private boolean modifyEvent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event);
        ((TextView)findViewById(R.id.textToolbar)).setText("Create an Event");
        ((ImageView)findViewById(R.id.toolbarLeftIcon)).setBackgroundResource(R.drawable.ic_edit);

        if(!((GlobalVars)this.getApplication()).isCreateModifyEventWelcome())  Voice.instancia().speak(getString(R.string.CrateModifyEventWelcome), TextToSpeech.QUEUE_FLUSH, null, "text");

        helpButton = findViewById(R.id.toolbarRightIcon);
        dialog = new Dialog(this);

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        model = (EventModel) getIntent().getSerializableExtra("EventModel");

        if(model != null) {
            this.modifyEvent = true;
            ((TextView)findViewById(R.id.createEventName)).setText(model.getEvent());
            ((TextView)findViewById(R.id.createEventDate)).setText(model.getDate());
            ((TextView)findViewById(R.id.createEventTime)).setText(model.getTime());
        }

        ((GlobalVars)this.getApplication()).setCreateModifyEventWelcome(true);

        final ImageButton microButton = findViewById(R.id.fab);

        microButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    microButton.setBackgroundResource(R.drawable.ic_grupo_48);
                    stopListening();
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    microButton.setBackgroundResource(R.drawable.ic_grupo_48_red);
                    startListening();
                }

                return false;
            }
        });
    }

    @Override
    public void getResult(String result) {

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        int action = Message.parseCreateModifyEvent(result);

        switch (action){
            case 0: // UNDEFINED COMMAND (DONE)
                undefinedCommand();
                break;
            case 1: // HELP (DONE)
                openDialog();
                break;
            case 2:  // SET THE NAME OF THE EVENT (DONE)
                setEventName(result);
                break;
            case 3: // SET THE DATE OF THE EVENT (DONE)
                setEventDate(result);
                break;
            case 4: // SET THE TIME OF THE EVENT (DONE)
                setEventTime(result);
                break;
            case 5: // CREATE THE EVENT (DONE)
                createEvent(result);
                break;
        }
    }

    /* COMMANDS ACTIONS METHODS */

    private void undefinedCommand() {
        Voice.instancia().speak(getString(R.string.UndefinedCommand), TextToSpeech.QUEUE_FLUSH, null, "text");
        if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
    }


    private void openDialog() {
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.help_dialog, null);
        TextView editText = (TextView) v.findViewById(R.id.helpText);
        editText.setText(R.string.CreateModifyEventHelp);
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0.2f);
        dialog.getWindow().getAttributes().gravity = Gravity.TOP;
        dialog.show();
        Voice.instancia().speak(getString(R.string.HelpMe), TextToSpeech.QUEUE_FLUSH, null, "text");
    }

    private void setEventName(String result){

        String nameOfEvent = Message.getAfterString("name is", result);

        if(nameOfEvent != null & nameOfEvent.length() > 0){

            ((TextView)findViewById(R.id.createEventName)).setText(nameOfEvent);

            Voice.instancia().speak(getString(R.string.NameDefined, "event"), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
        else{
            Voice.instancia().speak("Invalid name", TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }

    private void setEventDate(String result){

        String dayOfEvent = null;

        if(result.contains("st")){
            dayOfEvent = Message.getBetweenStrings("is ", "st", result);
        } else if(result.contains("nd")){
            dayOfEvent = Message.getBetweenStrings("is ", "nd", result);
        } else if(result.contains("rd")){
            dayOfEvent = Message.getBetweenStrings("is ", "rd", result);
        } else if(result.contains("th")){
            dayOfEvent = Message.getBetweenStrings("is ", "th", result);
        }

        String monthOfEvent = Message.getAfterString("of", result);
        if(monthOfEvent != null && dayOfEvent != null){
            Date date1 = null;
            try {
                date1 = new java.sql.Date(new SimpleDateFormat("MMMM", Locale.US).parse(monthOfEvent.replaceAll("\\s+", "")).getTime());
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);

                String date = Integer.parseInt(dayOfEvent) + "/" + String.valueOf(cal.get(Calendar.MONTH)+1);

                if(GlobalVars.goodDate(date)){
                    ((TextView)findViewById(R.id.createEventDate)).setText(date);
                    Voice.instancia().speak(getString(R.string.EventDateDefined), TextToSpeech.QUEUE_FLUSH, null, "text");
                } else{
                    Voice.instancia().speak("Invalid date", TextToSpeech.QUEUE_FLUSH, null, "text");
                    if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
                }
            } catch (ParseException e) {
                Voice.instancia().speak("Invalid date", TextToSpeech.QUEUE_FLUSH, null, "text");
                if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
            }
        }
        else{
            Voice.instancia().speak("Invalid date", TextToSpeech.QUEUE_FLUSH, null, "text");
            if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
        }
    }

    private void setEventTime(String result){
        String timeOfEvent = Message.getAfterString("is ", result);

        if(GlobalVars.isValidTime(timeOfEvent)){
            ((TextView)findViewById(R.id.createEventTime)).setText(timeOfEvent);
            Voice.instancia().speak(getString(R.string.EventDateDefined), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
        else{
            ((TextView)findViewById(R.id.createEventTime)).setText("all day");
            Voice.instancia().speak(getString(R.string.EventDateDefined), TextToSpeech.QUEUE_FLUSH, null, "text");
        }
    }

    private void createEvent(String result){

        String nameOfEvent = ((TextView)findViewById(R.id.createEventName)).getText().toString();
        String dateOfEvent = ((TextView)findViewById(R.id.createEventDate)).getText().toString();
        String timeOfEvent = ((TextView)findViewById(R.id.createEventTime)).getText().toString();

        if(nameOfEvent.length() != 0 && dateOfEvent.length() != 0){

            if(GlobalVars.goodDate(dateOfEvent)){
                if(GlobalVars.isValidTime(timeOfEvent)){
                    long eventId = eventId(nameOfEvent, dateOfEvent, timeOfEvent);
                    if(eventId != -1) {
                        if (GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneSuccess(this);
                        finish();
                    }
                    else{
                        Voice.instancia().speak("Insertion to calendar fail", TextToSpeech.QUEUE_FLUSH, null, "text");
                        if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
                    }
                }
                else{
                    long eventId = eventId(nameOfEvent, dateOfEvent, "all day");
                    if(eventId != -1) {
                        if (GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneSuccess(this);
                        finish();
                    }
                    else{
                        Voice.instancia().speak("Insertion to calendar fail", TextToSpeech.QUEUE_FLUSH, null, "text");
                        if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
                    }
                }
            }
            else{
                Voice.instancia().speak("Invalid date", TextToSpeech.QUEUE_FLUSH, null, "text");
                if(GlobalVars.isNotificationsEnable()) GlobalVars.ringtoneFailure(this);
            }
        }
        else{
            Voice.instancia().speak("Some field empty", TextToSpeech.QUEUE_FLUSH, null, "text");
        }

    }

    private long eventId(String name, String date, String time){
        long calID = ((GlobalVars)getApplication()).getIdCal();
        long startMillis = 0;
        ContentValues values = new ContentValues();
        Calendar beginTime = Calendar.getInstance();

        String[] dateSplit = date.split("/");

            if(time.contains("all day")){
                beginTime.set(Calendar.YEAR, 2021);
                beginTime.set(Calendar.MONTH, Integer.parseInt(dateSplit[1])-1);
                beginTime.set(Calendar.DATE, Integer.parseInt(dateSplit[0]));
                values.put(CalendarContract.Events.ALL_DAY, 1);
            }
            else if(Arrays.asList(time.split(":")).size() == 1){
                beginTime.set(Calendar.YEAR, 2021);
                beginTime.set(Calendar.MONTH, Integer.parseInt(dateSplit[1])-1);
                beginTime.set(Calendar.DATE, Integer.parseInt(dateSplit[0]));
                beginTime.set(Calendar.HOUR, Integer.parseInt(time.substring(0, time.lastIndexOf(' '))));
                beginTime.set(Calendar.MINUTE, 0);
                if(time.contains("p.m.")) beginTime.set(Calendar.AM_PM, Calendar.PM);
                else beginTime.set(Calendar.AM_PM, Calendar.AM);
            } else{
                beginTime.set(Calendar.YEAR, 2021);
                beginTime.set(Calendar.MONTH, Integer.parseInt(dateSplit[1])-1);
                beginTime.set(Calendar.DATE, Integer.parseInt(dateSplit[0]));
                beginTime.set(Calendar.HOUR, Integer.parseInt(time.substring(0, time.lastIndexOf(':'))));
                beginTime.set(Calendar.MINUTE, Integer.parseInt(Message.getBetweenStrings(":", " ", time)));
                if(time.contains("p.m.")) beginTime.set(Calendar.AM_PM, Calendar.PM);
                else beginTime.set(Calendar.AM_PM, Calendar.AM);
            }

            startMillis = beginTime.getTimeInMillis();

            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, startMillis);
            values.put(CalendarContract.Events.TITLE, name);
            values.put(CalendarContract.Events.CALENDAR_ID, calID);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Madrid");

            if(!modifyEvent){
                Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
// get the event ID that is the last element in the Uri
                return Long.parseLong(uri.getLastPathSegment());
            }
            else{
                ContentResolver cr = getContentResolver();
                ContentValues val = new ContentValues();
                Uri updateUri = null;
                // The new title for the event
                val.put(CalendarContract.Events.TITLE, name);
                val.put(CalendarContract.Events.DTSTART, startMillis);
                val.put(CalendarContract.Events.DTEND, startMillis);
                updateUri = ContentUris.withAppendedId(Uri.parse("content://com.android.calendar/events"), model.getEventId());
                int rows = cr.update(updateUri, val, null, null);
                return 1;
            }
    }
}
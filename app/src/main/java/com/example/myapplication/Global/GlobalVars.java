package com.example.myapplication.Global;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.myapplication.Model.EventModel;
import com.example.myapplication.NumberParser.NumberToWords;
import com.example.myapplication.R;
import com.orm.SugarApp;
import com.orm.SugarContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalVars extends SugarApp {

    private boolean MainToDoWelcome = false;
    private boolean CreateModiftyTaskWelcome = false;
    private boolean MainEventsWelcome = false;
    private boolean CreateModifyEventWelcome = false;
    private boolean MainShoppingListWelcome = false;
    private boolean CreateModifyShoppingList = false;
    static private long idCal;
    static private boolean NotificationsEnable = true;

    public long getIdCal() {
        return idCal;
    }

    public void setIdCal(long idCal) {
        this.idCal = idCal;
    }

    public boolean isMainToDoWelcome() {
        return MainToDoWelcome;
    }

    public void setMainToDoWelcome(boolean mainToDoWelcome) {
        MainToDoWelcome = mainToDoWelcome;
    }

    public boolean isCreateModiftyTaskWelcome() {
        return CreateModiftyTaskWelcome;
    }

    public void setCreateModiftyTaskWelcome(boolean createModiftyTaskWelcome) {
        CreateModiftyTaskWelcome = createModiftyTaskWelcome;
    }

    public boolean isMainEventsWelcome() {
        return MainEventsWelcome;
    }

    public void setMainEventsWelcome(boolean mainEventsWelcome) {
        MainEventsWelcome = mainEventsWelcome;
    }

    public boolean isCreateModifyEventWelcome() {
        return CreateModifyEventWelcome;
    }

    public void setCreateModifyEventWelcome(boolean createModifyEventWelcome) {
        CreateModifyEventWelcome = createModifyEventWelcome;
    }

    public boolean isMainShoppingListWelcome() {
        return MainShoppingListWelcome;
    }

    public void setMainShoppingListWelcome(boolean mainShoppingListWelcome) {
        MainShoppingListWelcome = mainShoppingListWelcome;
    }

    public boolean isCreateModifyShoppingList() {
        return CreateModifyShoppingList;
    }

    public void setCreateModifyShoppingList(boolean createModifyShoppingList) {
        CreateModifyShoppingList = createModifyShoppingList;
    }

    static public void ringtoneSuccess(Context c){

        try{
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(c, notification);
            r.play();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static public void ringtoneFailure(Context c){

        try{
            MediaPlayer media = MediaPlayer.create(c, R.raw.error_notification);
            media.start();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    static public boolean isNotificationsEnable() {
        return NotificationsEnable;
    }

    static public void setNotificationsEnable(boolean notificationsEnable) {
        NotificationsEnable = notificationsEnable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    static public int dayWordToInt(String day){

        for(int i=1; i < 32; i++){
            if(NumberToWords.convert(i).equalsIgnoreCase(day)) return i;
        }

        return 0;
    }

    static public int idWordToInt(String id, List<EventModel> eventList){

        for (EventModel event: eventList) {
            if(NumberToWords.convert(event.getId()).equalsIgnoreCase(id)) return Math.toIntExact(event.getId());
        }

        return 0;
    }

    public static boolean goodDate(String date){

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM");
            format.setLenient(false);
            format.parse(date);
        } catch(ParseException e){
            return false;
        }

        return true;
    }

    public static boolean isValidTime(String time)
    {

        // Regex to check valid time in 12-hour format.
        String regexPattern = "((((0[0-9]|[0-9]|1[01]):" + "[0-5][0-9])(\\s))|((0[0-9]|[0-9]|1[01])(\\s)))" + "?(?i)(a.m.|p.m.)";

        // Compile the ReGex
        Pattern compiledPattern = Pattern.compile(regexPattern);

        // If the time is empty
        // return false
        if (time == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given time
        // and regular expression.
        Matcher m = compiledPattern.matcher(time);

        // Return if the time
        // matched the ReGex
        return m.matches();
    }
}

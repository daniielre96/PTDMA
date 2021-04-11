package com.example.myapplication.Global;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.myapplication.R;
import com.orm.SugarApp;
import com.orm.SugarContext;

public class GlobalVars extends SugarApp {

    private boolean MainToDoWelcome = false;
    private boolean CreateModiftyTaskWelcome = false;
    private boolean MainEventsWelcome = false;
    private boolean CreateModifyEventWelcome = false;
    private boolean MainShoppingListWelcome = false;
    private boolean CreateModifyShoppingList = false;
    static private boolean NotificationsEnable = true;

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
}

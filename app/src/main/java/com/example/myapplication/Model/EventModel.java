package com.example.myapplication.Model;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class EventModel extends SugarRecord implements Serializable {
    private int status;
    private String event;
    private long eventId;

    private String date;
    private String time;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}

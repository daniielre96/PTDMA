package com.example.myapplication.Model;

import com.orm.SugarRecord;

import java.util.Date;

public class EventModel extends SugarRecord{
    private int status;
    private String event;

    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

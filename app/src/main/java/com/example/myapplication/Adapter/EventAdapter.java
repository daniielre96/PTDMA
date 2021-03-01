package com.example.myapplication.Adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainEvents;
import com.example.myapplication.MainMenu;
import com.example.myapplication.MainTasks;
import com.example.myapplication.Model.EventModel;
import com.example.myapplication.R;

import java.util.Calendar;
import java.util.List;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private MainEvents activity;

    public EventAdapter(MainEvents activity){ this.activity = activity; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        EventModel item = eventList.get(position);
        holder.event.setText(item.getEvent());
        holder.day.setText(item.getDay());
        holder.month.setText(item.getMonth());
    }

    public int getItemCount() { return eventList.size(); }

    public void setEvents(List<EventModel> eventList){
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView day, month, event;
        CardView cardView;

        ViewHolder(View view){
            super(view);
            day = view.findViewById(R.id.eventDay);
            month = view.findViewById(R.id.eventMonth);
            event = view.findViewById(R.id.eventText);
            cardView = view.findViewById(R.id.eventLayout);
        }

    }
}

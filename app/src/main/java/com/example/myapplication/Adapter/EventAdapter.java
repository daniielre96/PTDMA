package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.format.DateFormat;
import android.util.Log;
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

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private Context activity;

    public EventAdapter(Context activity){ this.activity = activity; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);

        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        EventModel item = eventList.get(position);
        holder.event.setText(String.valueOf(item.getId()) + ". " + item.getEvent());
        holder.day.setText((String) DateFormat.format("dd", item.getDate()));
        holder.month.setText(((String) DateFormat.format("MMM", item.getDate())).toUpperCase());

        Date today = Calendar.getInstance().getTime();

        System.out.println("Date today month:" + Integer.parseInt((String)DateFormat.format("MM", today)));
        System.out.println("Date event month:" + Integer.parseInt((String)DateFormat.format("MM", item.getDate())));

        if(Integer.parseInt((String)DateFormat.format("MM", today)) >= Integer.parseInt((String)DateFormat.format("MM", item.getDate()))){

            if(Integer.parseInt((String)DateFormat.format("MM", today)) > Integer.parseInt((String)DateFormat.format("MM", item.getDate()))){
                holder.event.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.cardView.setBackgroundColor(Color.parseColor("#CACACA"));
            }
            else{ // ==
                if(Integer.parseInt((String)DateFormat.format("dd", today)) > Integer.parseInt((String)DateFormat.format("dd", item.getDate()))){
                    holder.event.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.cardView.setBackgroundColor(Color.parseColor("#CACACA"));
                }
                else{
                    holder.event.setPaintFlags(0);
                    holder.cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        }
        else{
            holder.event.setPaintFlags(0);
            holder.cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    public int getItemCount() { return eventList.size(); }

    public void setEvents(List<EventModel> eventList){
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView day, month, event;
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            day = view.findViewById(R.id.eventDay);
            month = view.findViewById(R.id.eventMonth);
            event = view.findViewById(R.id.eventText);
            cardView = view.findViewById(R.id.eventLayout);
        }

    }
}

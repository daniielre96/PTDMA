package com.example.myapplication.Adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainTasks;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.R;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainTasks activity;

    public ToDoAdapter(MainTasks activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
    }

    public int getItemCount() {
        return todoList.size();
    }

    private boolean toBoolean(int n){
        return n != 0;
    }

    public void setTasks(List<ToDoModel> todoList){
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;
        CardView cardView;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            cardView = view.findViewById(R.id.taskLayout);

            task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        task.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        cardView.setBackgroundColor(Color.parseColor("#CACACA"));
                    }else{
                        task.setPaintFlags(0);
                        cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });
        }

    }
}

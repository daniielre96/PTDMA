package com.example.myapplication.Adapter;

import android.content.Context;
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

import com.example.myapplication.Model.ItemModel;
import com.example.myapplication.Model.ToDoModel;
import com.example.myapplication.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private List<ItemModel> itemList;
    private Context activity;

    public ItemAdapter(Context activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ItemAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position){
        ItemModel item = itemList.get(position);
        holder.item.setText(item.getName());

        holder.item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    holder.item.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.cardView.setBackgroundColor(Color.parseColor("#CACACA"));
                    item.setStatus(1);
                    item.save();
                }else{
                    holder.item.setPaintFlags(0);
                    holder.cardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    item.setStatus(0);
                    item.save();
                }
            }
        });

        holder.item.setChecked(toBoolean(item.getStatus()));
    }

    public int getItemCount() {
        return itemList.size();
    }

    private boolean toBoolean(int n){
        return n != 0;
    }

    public void setItems(List<ItemModel> itemList){
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox item;
        CardView cardView;

        ViewHolder(View view){
            super(view);

            item = view.findViewById(R.id.todoCheckBox);
            cardView = view.findViewById(R.id.taskLayout);

        }

    }
}

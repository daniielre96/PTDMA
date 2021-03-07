package com.example.myapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainShoppingList;
import com.example.myapplication.Model.EventModel;
import com.example.myapplication.Model.ShoppingModel;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {

    private List<ShoppingModel> shoppingList;
    private MainShoppingList activity;

    public ShoppingAdapter(MainShoppingList activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_grid_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingModel item = shoppingList.get(position);
        holder.title.setText(item.getTitle());
        holder.gridIcon.setImageResource(item.getImage());
    }

    @Override
    public int getItemCount() {
        return this.shoppingList.size();
    }

    public void setShoppingList(List<ShoppingModel> shoppingList){
        this.shoppingList = shoppingList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView gridIcon;

        public ViewHolder(@NonNull View itemView) {
            
            super(itemView);
            title = itemView.findViewById(R.id.textView2);
            gridIcon = itemView.findViewById(R.id.imageView2);
        }
    }
}



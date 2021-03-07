package com.example.myapplication.Model;

import com.example.myapplication.Adapter.ToDoAdapter;

import java.util.ArrayList;

public class ShoppingModel {
    private int id, image;
    private String title;
    private ArrayList<ToDoModel> items;

    public ArrayList<ToDoModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ToDoModel> items) {
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

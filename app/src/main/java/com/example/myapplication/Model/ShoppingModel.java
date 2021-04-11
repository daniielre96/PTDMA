package com.example.myapplication.Model;

import com.example.myapplication.Adapter.ToDoAdapter;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;

public class ShoppingModel extends SugarRecord {
    private int image;
    private String title;
    @Ignore
    private ArrayList<ItemModel> items;

    public ArrayList<ItemModel> getItems() {

        if(items == null){
            items = (ArrayList<ItemModel>) ItemModel.getByList(this.getId());
            if(items == null) items = new ArrayList<ItemModel>();
        }

        return items;
    }

    public void setItems(ArrayList<ItemModel> items) {
        this.items = items;
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

    @Override
    public long save() {

        long id = super.save();

        if(!getItems().isEmpty()){
            getItems().forEach(it -> {
                it.setId(id);
                it.save();
            });
        }

        return id;
    }

    @Override
    public boolean delete() {

        super.delete();

        if(!getItems().isEmpty()){
            getItems().forEach(it -> it.delete());
        }

        return true;
    }

    public static ShoppingModel getIfExists(String name){
        return Select.from(ShoppingModel.class).where(Condition.prop("title").eq(name)).first();
    }

}

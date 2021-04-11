package com.example.myapplication.Model;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.Serializable;
import java.util.List;

public class ItemModel  extends SugarRecord implements Serializable{
    private int status;
    private String name;
    private long idList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdList() {
        return idList;
    }

    public void setIdList(long idList) {
        this.idList = idList;
    }

    public static List<ItemModel> getByList(long id){

        return Select.from(ItemModel.class).where(Condition.prop("ID_LIST").eq(id)).list();
    }
}

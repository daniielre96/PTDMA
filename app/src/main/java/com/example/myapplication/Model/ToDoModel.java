package com.example.myapplication.Model;

import androidx.annotation.Nullable;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.Serializable;

public class ToDoModel extends SugarRecord implements Serializable {
    private int status;
    private String task;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public static ToDoModel getIfExists(String name){
        return Select.from(ToDoModel.class).where(Condition.prop("task").eq(name)).first();
    }
}

package com.example.david.todohw4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

public class ToDo implements Serializable {
    private String title;
    private int id;
    private String description;
    private String datecreated;
    boolean selected = false;


    public ToDo(String title, String description, String datecreated, boolean selected) {
        super();
        this.title = title;
        this.description = description;
        this.datecreated = datecreated;
        this.selected = selected;
    }

    public int getID(){return id;}
    public void setID(int id){ this.id= id; }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatecreated() {
        return datecreated;
    }
    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return title;
    }
}

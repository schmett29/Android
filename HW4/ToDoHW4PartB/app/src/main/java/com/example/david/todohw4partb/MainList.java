package com.example.david.todohw4partb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

public class MainList implements Serializable {

    private String title;
    private int id;
    boolean selected = false;


    public MainList(String title,  boolean selected) {
        super();
        this.title = title;
        this.selected = selected;
    }

    public int getID(){return id;}
    public void setID(int id){ this.id= id; }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
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

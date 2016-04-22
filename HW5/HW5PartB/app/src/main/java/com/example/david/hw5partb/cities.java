package com.example.david.hw5partb;

import java.io.Serializable;

/**
 * Created by David on 4/9/16.
 */
public class cities implements Serializable{
    //MEMBER ATTRIBUTES
    private String name;
    private String pop1,pop2;
    private int id;
    private int diff;

    public cities(String c, String p1, String p2) {
        super();
        this.name = c;
        this.pop1 = p1;
        this.pop2 = p2;
    }


    public String getCity() {
        return  name;
    }
    public String getPop1() {
        return pop1;
    }
    public String getPop2() {
        return pop2;
    }
    public int getID(){return id;}
    public void setID(int id){ this.id= id; }

    public int getDiff(){return diff;}
    public void setDiff(int diff){ this.diff= diff; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPop1(String pop1) {
        this.pop1 = pop1;
    }

    public void setPop2(String pop2) {
        this.pop2 = pop2;
    }

    @Override
    public String toString() {
        return "" + this.getCity() + " " + this.getPop1() + " " + this.getPop2() ;
    }
}

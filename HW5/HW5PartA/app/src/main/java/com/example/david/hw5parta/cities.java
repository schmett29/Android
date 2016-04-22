package com.example.david.hw5parta;

/**
 * Created by David on 4/9/16.
 */
public class cities {
    //MEMBER ATTRIBUTES
    private String name;
    private String pop1,pop2;

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

    @Override
    public String toString() {
        return "" + this.getCity() + " " + this.getPop1() + " " + this.getPop2() ;
    }
}

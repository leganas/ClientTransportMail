package com.leganas.mobile;

/**
 * Created by AndreyLS on 24.01.2017.
 */

public class Item
{
    String t;

    public Item(Integer i) {
        t = i.toString();
    }

    public String getT(){
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}

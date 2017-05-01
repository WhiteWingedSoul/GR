package com.hedspi.hoangviet.eslrecom.models;

/**
 * Created by viet on 4/27/17.
 */

public class KanseiItem {
    private String name;
    private double value;
    private int totalTimeRated = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void addValue(double value){
        this.value = value;
        totalTimeRated ++;
    }

    public Double retrieveValue(){
        if (totalTimeRated != 0)
            return value/totalTimeRated;
        else return 0.0;
    }

    public int getTotalTimeRated() {
        return totalTimeRated;
    }

}

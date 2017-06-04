package com.hedspi.hoangviet.eslrecom.models;

/**
 * Created by viet on 4/27/17.
 */

public class KanseiKeyword {
    private String name;
    private double value = 0;
    private int totalTimeRated;

    private double goodScore = 0;
    private int totalGoodRated = 0;
    private double badScore = 0;
    private int totalBadRated = 0;

    private static final int CURVE_RATE = 2;

    public int getTotalGoodRated() {
        return totalGoodRated;
    }

    public void setTotalGoodRated(int totalGoodRated) {
        this.totalGoodRated = totalGoodRated;
    }

    public int getTotalBadRated() {
        return totalBadRated;
    }

    public void setTotalBadRated(int totalBadRated) {
        this.totalBadRated = totalBadRated;
    }

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

    public int getTotalTimeRated() {
        return totalTimeRated;
    }

    public void addValue(double value){
        this.value += value;
        totalTimeRated ++;
    }

    public void addGoodScore(double value){
        this.goodScore += value;
        totalGoodRated ++;
    }

    public void addBadScore(double value){
        this.badScore += value;
        totalBadRated ++;
    }

    public Double retrieveGoodScore(){
        if (totalGoodRated != 0)
            return (goodScore/totalGoodRated)* (1/(1+CURVE_RATE*Math.exp(-totalGoodRated)));
        else return 0.0;
    }

    public Double retrieveBadScore(){
        if (totalBadRated != 0)
            return (badScore/totalBadRated)* (1/(1+CURVE_RATE*Math.exp(-totalBadRated)));
        else return 0.0;
    }

    public Double retrieveValue(){
        //TODO TESTING

        if (Math.abs(retrieveGoodScore()*totalGoodRated)>=Math.abs(retrieveBadScore()*totalBadRated))
            return retrieveGoodScore();
        else
            return retrieveBadScore();
        // GOOD CODE
//        if (totalTimeRated != 0)
//            return (value/totalTimeRated)* (1/(1+CURVE_RATE*Math.exp(-totalTimeRated)));
//        else return 0.0;
    }

    public int retrieveTotalTimeRated() {
        return totalBadRated+totalGoodRated;
    }



}

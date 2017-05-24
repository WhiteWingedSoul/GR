package com.hedspi.hoangviet.eslrecom.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 5/23/17.
 */

public class KanseiItem {
    private List<KanseiKeyword> keywords;
    private String target;

    private double totalRateScore;
    private int totalRateTime;

    public void init(){
        keywords = new ArrayList<>();
    }

    public KanseiKeyword retrieveKeyword(String name){
        if (keywords != null && keywords.size() != 0){
            for (KanseiKeyword item : keywords){
                if (item.getName().equals(name))
                    return item;
            }
        }
        return null;
    }

    public void addScore(double score){
        totalRateScore += Math.abs(score);
        totalRateTime++;
    }

    public double retrieveWeight(){
        if (totalRateTime!=0)
            return totalRateScore/totalRateTime;
        else
            return 0;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<KanseiKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KanseiKeyword> keywords) {
        this.keywords = keywords;
    }

}


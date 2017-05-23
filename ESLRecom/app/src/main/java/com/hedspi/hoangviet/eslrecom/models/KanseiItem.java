package com.hedspi.hoangviet.eslrecom.models;

import java.util.List;

/**
 * Created by viet on 5/23/17.
 */

public class KanseiItem {
    private List<KanseiKeyword> keywords;
    private double weight;

    public KanseiKeyword retrieveKeyword(String name){
        if (keywords != null && keywords.size() != 0){
            for (KanseiKeyword item : keywords){
                if (item.getName().equals(name))
                    return item;
            }
        }
        return null;
    }
    public List<KanseiKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<KanseiKeyword> keywords) {
        this.keywords = keywords;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}


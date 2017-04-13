package com.hedspi.hoangviet.eslrecom.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 3/17/17.
 */

public class Tag implements Serializable{
    @SerializedName("name")
    private String name;
    @SerializedName("relevantTag")
    private List<ChildTag> relevantTag;
    @SerializedName("score")
    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildTag> getRelevantTag() {
        return relevantTag;
    }

    public void setRelevantTag(List<ChildTag> relevantTag) {
        this.relevantTag = relevantTag;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

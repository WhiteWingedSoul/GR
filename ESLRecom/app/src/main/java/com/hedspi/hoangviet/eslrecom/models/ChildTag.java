package com.hedspi.hoangviet.eslrecom.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by viet on 3/17/17.
 */

public class ChildTag  implements Serializable{
    @SerializedName("name")
    private String name;
    @SerializedName("score")
    private long score;
    @SerializedName("realScore")
    private long realScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getRealScore() {
        return realScore;
    }

    public void setRealScore(long realScore) {
        this.realScore = realScore;
    }
}

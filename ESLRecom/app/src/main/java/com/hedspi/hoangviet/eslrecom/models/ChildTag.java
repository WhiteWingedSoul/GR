package com.hedspi.hoangviet.eslrecom.models;

import java.io.Serializable;

/**
 * Created by viet on 3/17/17.
 */

public class ChildTag  implements Serializable{
    private String name;
    private int score;
    private int realScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRealScore() {
        return realScore;
    }

    public void setRealScore(int realScore) {
        this.realScore = realScore;
    }
}

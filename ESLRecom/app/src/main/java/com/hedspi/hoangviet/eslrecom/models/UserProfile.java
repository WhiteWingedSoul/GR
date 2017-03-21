package com.hedspi.hoangviet.eslrecom.models;

import com.hedspi.hoangviet.eslrecom.commons.Preference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 11/21/16.
 */

public class UserProfile implements Serializable {
    private double learnTimeScore;
    private double readingScore;
    private double writtingScore;
    private double listeningScore;
    private double speakingScore;
    private double overallScore;
//    private int timeCanSpend;
//    private int testPreference;
    private List<String> learnList = new ArrayList<>();

    public static final double BEGINNER = 0;
    public static final double BASIC = 0.3;
    public static final double INTERMEDIATE = 0.5;
    public static final double ADVANCE = 0.8;
    public static final double MASTER = 1;

    public static final double MATCH_SCORE_OVERALL = 0.37;
    public static final double MATCH_SCORE_LEARNLIST = 0.78;
//    public static final double MATCH_SCORE_TIMESPEND = 0.2;
//    public static final double MATCH_SCORE_TESTPREFER = 0.33;

    public double getReadingScore() {
        return readingScore;
    }

    public void setReadingScore(double readingScore) {
        this.readingScore = readingScore;
    }

    public double getWrittingScore() {
        return writtingScore;
    }

    public void setWrittingScore(double writtingScore) {
        this.writtingScore = writtingScore;
    }

    public double getListeningScore() {
        return listeningScore;
    }

    public void setListeningScore(double listeningScore) {
        this.listeningScore = listeningScore;
    }

    public double getSpeakingScore() {
        return speakingScore;
    }

    public void setSpeakingScore(double speakingScore) {
        this.speakingScore = speakingScore;
    }

    public double getOverallScore() {
        overallScore = learnTimeScore*0.1 + readingScore*0.225 + listeningScore*0.225 + writtingScore*0.225 + speakingScore *0.225;
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

//    public int getTimeCanSpend() {
//        return timeCanSpend;
//    }
//
//    public void setTimeCanSpend(int timeCanSpend) {
//        this.timeCanSpend = timeCanSpend;
//    }
//
//    public int getTestPreference() {
//        return testPreference;
//    }
//
//    public void setTestPreference(int testPreference) {
//        this.testPreference = testPreference;
//    }

    public List<String> getLearnList() {
        return learnList;
    }

    public void setLearnList(List<String> learnList) {
        this.learnList = learnList;
    }

    public double getLearnTimeScore() {
        return learnTimeScore;
    }

    public void setLearnTimeScore(double learnTimeScore) {
        this.learnTimeScore = learnTimeScore;
    }

    public String getOverallPreference(){
        if(getOverallScore() == 0)
            return Preference.BEGINNER;
        else if(getOverallScore()<=0.3)
            return Preference.ELEMENTARY;
        else if (getOverallScore()<=0.75)
            return Preference.INTERMEDIATE;
        else return Preference.ADVANCE;
    }

    public double getBestMatch(){
        return MATCH_SCORE_LEARNLIST+MATCH_SCORE_OVERALL; //+MATCH_SCORE_TIMESPEND+MATCH_SCORE_TESTPREFER;
    }
}

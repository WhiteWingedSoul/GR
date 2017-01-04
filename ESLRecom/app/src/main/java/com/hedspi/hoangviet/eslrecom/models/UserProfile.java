package com.hedspi.hoangviet.eslrecom.models;

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
    private int timeCanSpend;
    private String testPreference;
    private List<String> learnList = new ArrayList<>();

    public static final double BEGINNER = 0;
    public static final double BASIC = 0.3;
    public static final double INTERMEDIATE = 0.5;
    public static final double ADVANCE = 0.8;
    public static final double MASTER = 1;

    public static final int LITTLE = 1;
    public static final int NORMAL = 2;
    public static final int ALOT = 3;

    public static final double MATCH_SCORE_OVERALL = 0.6213;
    public static final double MATCH_SCORE_READING = 0.22131;
    public static final double MATCH_SCORE_LISTENING = 0.22131;
    public static final double MATCH_SCORE_WRITTING = 0.22131;
    public static final double MATCH_SCORE_SPEAKING = 0.22131;
    public static final double MATCH_SCORE_LEARNLIST = 0.884239;
    public static final double MATCH_SCORE_TIMESPEND = 0;
    public static final double MATCH_SCORE_TESTPREFER = 0;

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

    public int getTimeCanSpend() {
        return timeCanSpend;
    }

    public void setTimeCanSpend(int timeCanSpend) {
        this.timeCanSpend = timeCanSpend;
    }

    public String getTestPreference() {
        return testPreference;
    }

    public void setTestPreference(String testPreference) {
        this.testPreference = testPreference;
    }

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

    public List<String> getSearchQuery(){
        List<String> listQuery = new ArrayList<>();
        String query;
        for(String learnType : getLearnList()) {
            query = getOverallPreference() + " english " + learnType;
            listQuery.add(query);

            if (getTestPreference()!=null){
                query = getOverallPreference() + " " +getTestPreference() + " " + learnType;
                listQuery.add(query);
            }
        }
        return listQuery;
    }

    public String getReadingPreference(){
        if(getOverallScore() == 0)
            return "beginner";
        else if(getOverallScore()<=0.3)
            return "basic";
        else if (getOverallScore()<=0.6)
            return "intermediate";
        else if (getOverallScore()<=0.9)
            return "advance";
        else return "high level";
    }

    public String getWrittingPreference(){
        if(getOverallScore() == 0)
            return "begin";
        else if(getOverallScore()<=0.3)
            return "basic";
        else if (getOverallScore()<=0.6)
            return "intermediate";
        else if (getOverallScore()<=0.9)
            return "advance";
        else return "high level";
    }
    public String getListeningPreference(){
        if(getOverallScore() == 0)
            return "begin";
        else if(getOverallScore()<=0.3)
            return "basic";
        else if (getOverallScore()<=0.6)
            return "intermediate";
        else if (getOverallScore()<=0.9)
            return "advanced";
        else return "high level";
    }
    public String getSpeakingPreference(){
        if(getOverallScore() == 0)
            return "begin";
        else if(getOverallScore()<=0.3)
            return "basic";
        else if (getOverallScore()<=0.6)
            return "intermediate";
        else if (getOverallScore()<=0.9)
            return "advanced";
        else return "high level";
    }
    public String getOverallPreference(){
        if(getOverallScore() == 0)
            return "begin";
        else if(getOverallScore()<=0.3)
            return "basic";
        else if (getOverallScore()<=0.6)
            return "intermediate";
        else if (getOverallScore()<=0.9)
            return "advanced";
        else return "high level";
    }

    public double getBestMatch(){
        if (getTestPreference()==null){
         return MATCH_SCORE_LEARNLIST+MATCH_SCORE_LISTENING+MATCH_SCORE_OVERALL+MATCH_SCORE_READING+MATCH_SCORE_SPEAKING
                 +MATCH_SCORE_TIMESPEND+MATCH_SCORE_WRITTING;
        }else{
            return MATCH_SCORE_LEARNLIST+MATCH_SCORE_LISTENING+MATCH_SCORE_OVERALL+MATCH_SCORE_READING+MATCH_SCORE_SPEAKING
                    +MATCH_SCORE_TIMESPEND+MATCH_SCORE_WRITTING+MATCH_SCORE_TESTPREFER;
        }
    }
}

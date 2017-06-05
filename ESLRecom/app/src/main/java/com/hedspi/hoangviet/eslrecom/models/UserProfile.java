package com.hedspi.hoangviet.eslrecom.models;

import com.hedspi.hoangviet.eslrecom.commons.Preference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 11/21/16.
 */

public class UserProfile extends AdapterItem implements Serializable {
    private double readingScore;
    private double grammarScore;
    private double vocabularyScore;
    private double overallScore;
    private List<String> learnList = new ArrayList<>();
    private String role;
    private String uid;
    private String name;
    private String email;
    private String avatarLink;
    private boolean testProf = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public boolean isTestProf() {
        return testProf;
    }

    public void setTestProf(boolean testProf) {
        this.testProf = testProf;
    }

    public void setReadingScore(double readingScore) {
        this.readingScore = readingScore;
    }

    public void setGrammarScore(double grammarScore) {
        this.grammarScore = grammarScore;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public String getRole() {
        return role;
    }

    public String getUid() {
        return uid;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public static final double BEGINNER = 0;
    public static final double BASIC = 0.3;
    public static final double INTERMEDIATE = 0.5;
    public static final double UPPER_INTERMEDIATE = 0.8;
    public static final double ADVANCED = 1;

    public static final double MATCH_SCORE_OVERALL = 0.37;
    public static final double MATCH_SCORE_LEARNLIST = 0.78;
    public static final double MATCH_SCORE_DOCTYPE = 0.24;
//    public static final double MATCH_SCORE_TIMESPEND = 0.2;
//    public static final double MATCH_SCORE_TESTPREFER = 0.33;

    public double getReadingScore() {
        return readingScore;
    }

    public void calculateReadingScore(double readingScore) {
        this.readingScore = readingScore;
    }

    public void calculateReadingScore(String level) {
        switch (level){
            case Preference.BEGINNER:
                readingScore = BEGINNER;
                break;
            case Preference.ELEMENTARY:
                readingScore = BASIC;
                break;
            case Preference.INTERMEDIATE:
                readingScore = INTERMEDIATE;
                break;
            case Preference.UPPER_INTERMEDIATE:
                readingScore = UPPER_INTERMEDIATE;
                break;
            case Preference.ADVANCE:
                readingScore = ADVANCED;
                break;
        }
    }

    public double getGrammarScore() {
        return grammarScore;
    }

    public void calculateGrammarScore(double grammarScore) {
        this.grammarScore = grammarScore;
    }

    public void calculateGrammarScore(String level) {
        switch (level){
            case Preference.BEGINNER:
                grammarScore = BEGINNER;
                break;
            case Preference.ELEMENTARY:
                grammarScore = BASIC;
                break;
            case Preference.INTERMEDIATE:
                grammarScore = INTERMEDIATE;
                break;
            case Preference.UPPER_INTERMEDIATE:
                grammarScore = UPPER_INTERMEDIATE;
                break;
            case Preference.ADVANCE:
                grammarScore = ADVANCED;
                break;
        }
    }

    public double getVocabularyScore() {
        return vocabularyScore;
    }

    public void setVocabularyScore(double vocabularyScore) {
        this.vocabularyScore = vocabularyScore;
    }

    public void calculateVocabularyScore(String level) {
        switch (level){
            case Preference.BEGINNER:
                vocabularyScore = BEGINNER;
                break;
            case Preference.ELEMENTARY:
                vocabularyScore = BASIC;
                break;
            case Preference.INTERMEDIATE:
                vocabularyScore = INTERMEDIATE;
                break;
            case Preference.UPPER_INTERMEDIATE:
                vocabularyScore = UPPER_INTERMEDIATE;
                break;
            case Preference.ADVANCE:
                vocabularyScore = ADVANCED;
                break;
        }
    }

    public double calculateOverallScore() {
        overallScore = (readingScore + vocabularyScore + grammarScore)/3;
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

    public String returnOverallPreference(){
        if(calculateOverallScore() == 0)
            return Preference.BEGINNER;
        else if(calculateOverallScore()<=0.3)
            return Preference.ELEMENTARY;
        else if (calculateOverallScore()<=0.8)
            return Preference.INTERMEDIATE;
        else return Preference.ADVANCE;
    }

    public double calculateBestMatch(){
        return MATCH_SCORE_LEARNLIST+MATCH_SCORE_OVERALL; //+MATCH_SCORE_TIMESPEND+MATCH_SCORE_TESTPREFER;
    }
}

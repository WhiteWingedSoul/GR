package com.hedspi.hoangviet.eslrecom.commons;

/**
 * Created by viet on 1/3/17.
 */

public class Preference {
    public static final int BEGINNER = 1;
    public static final int BASIC = 2;
    public static final int INTERMEDIATE = 3;
    public static final int ADVANCE = 4;
    public static final int MASTER = 5;

    public static final int NONE = 1;
    public static final int IELTS = 2;
    public static final int TOEFL = 3;
    public static final int TOEIC = 4;
    public static final int iTEC = 5;

    public static final int LESS1= 1;
    public static final int FROM1TO3 = 2;
    public static final int MORE3= 3;

    private double overallScore;
    private double testScore;
    private double timeScore;
    private double learnPreferenceScore;
    private double decisionBoundary;

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    public double getTestScore() {
        return testScore;
    }

    public void setTestScore(double testScore) {
        this.testScore = testScore;
    }

    public double getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(double timeScore) {
        this.timeScore = timeScore;
    }

    public double getLearnPreferenceScore() {
        return learnPreferenceScore;
    }

    public void setLearnPreferenceScore(double learnPreferenceScore) {
        this.learnPreferenceScore = learnPreferenceScore;
    }

    public double getDecisionBoundary() {
        return decisionBoundary;
    }

    public void setDecisionBoundary(double decisionBoundary) {
        this.decisionBoundary = decisionBoundary;
    }

    public double getBestMatch(){
        return overallScore+testScore+timeScore+learnPreferenceScore;
    }
}

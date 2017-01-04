package com.hedspi.hoangviet.eslrecom.models;

import com.hedspi.hoangviet.eslrecom.libraries.GooglePlayElement;

/**
 * Created by hoangviet on 11/21/16.
 */

public class MatchResult {
    private GooglePlayElement element;

    private double readingMatch;
    private double writtingMatch;
    private double listeningMatch;
    private double speakingMatch;
    private double overallMatch;
    private double testPreferMatch;
    //asdjasdkjasdkjakklalkkkkawljwakjlkawjasc
    private double timeSpendMatch;
    private double learnListMatch;

    public double getReadingMatch() {
        return readingMatch;
    }

    public void setReadingMatch(double readingMatch) {
        this.readingMatch = readingMatch;
    }

    public double getWrittingMatch() {
        return writtingMatch;
    }

    public void setWrittingMatch(double writtingMatch) {
        this.writtingMatch = writtingMatch;
    }

    public double getListeningMatch() {
        return listeningMatch;
    }

    public void setListeningMatch(double listeningMatch) {
        this.listeningMatch = listeningMatch;
    }

    public double getSpeakingMatch() {
        return speakingMatch;
    }

    public void setSpeakingMatch(double speakingMatch) {
        this.speakingMatch = speakingMatch;
    }

    public double getOverallMatch() {
        return overallMatch;
    }

    public void setOverallMatch(double overallMatch) {
        this.overallMatch = overallMatch;
    }

    public double getTestPreferMatch() {
        return testPreferMatch;
    }

    public void setTestPreferMatch(double testPreferMatch) {
        this.testPreferMatch = testPreferMatch;
    }

    public double getTimeSpendMatch() {
        return timeSpendMatch;
    }

    public void setTimeSpendMatch(double timeSpendMatch) {
        this.timeSpendMatch = timeSpendMatch;
    }

    public double getLearnListMatch() {
        return learnListMatch;
    }

    public void setLearnListMatch(double learnListMatch) {
        this.learnListMatch = learnListMatch;
    }

    private double matchScore;

    public GooglePlayElement getElement() {
        return element;
    }

    public void setElement(GooglePlayElement element) {
        this.element = element;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }
}

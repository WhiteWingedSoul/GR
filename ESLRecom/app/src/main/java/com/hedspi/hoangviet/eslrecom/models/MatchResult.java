package com.hedspi.hoangviet.eslrecom.models;

/**
 * Created by hoangviet on 11/21/16.
 */

public class MatchResult {
    private Material book;
    private double matchScore;

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public Material getBook() {
        return book;
    }

    public void setBook(Material book) {
        this.book = book;
    }
}

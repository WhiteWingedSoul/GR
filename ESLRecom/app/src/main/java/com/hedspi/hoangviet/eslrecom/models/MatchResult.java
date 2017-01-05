package com.hedspi.hoangviet.eslrecom.models;

import com.hedspi.hoangviet.eslrecom.libraries.GooglePlayElement;

/**
 * Created by hoangviet on 11/21/16.
 */

public class MatchResult {
    private Book book;
    private double matchScore;

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}

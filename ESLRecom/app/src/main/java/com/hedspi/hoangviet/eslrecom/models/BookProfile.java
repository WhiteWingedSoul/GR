package com.hedspi.hoangviet.eslrecom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 1/4/17.
 */

public class BookProfile implements Serializable{
    private int id;
    private Book book;
    private int levelPreference = 0;
    private int timePreference = 0;
    private int testPreference = 0;
    private List<String> learnSubjectPreference = new ArrayList<>();;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getLevelPreference() {
        return levelPreference;
    }

    public void setLevelPreference(int levelPreference) {
        this.levelPreference = levelPreference;
    }

    public int getTimePreference() {
        return timePreference;
    }

    public void setTimePreference(int timePreference) {
        this.timePreference = timePreference;
    }

    public int getTestPreference() {
        return testPreference;
    }

    public void setTestPreference(int testPreference) {
        this.testPreference = testPreference;
    }

    public List<String> getLearnSubjectPreference() {
        return learnSubjectPreference;
    }

    public void setLearnSubjectPreference(List<String> learnSubjectPreference) {
        this.learnSubjectPreference = learnSubjectPreference;
    }
}

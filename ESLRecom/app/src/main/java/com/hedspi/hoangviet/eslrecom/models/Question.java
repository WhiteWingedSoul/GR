package com.hedspi.hoangviet.eslrecom.models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 4/13/17.
 */

public class Question implements Serializable{
    @SerializedName("question")
    private String question;
    @SerializedName("trueAnswer")
    private String trueAnswer;
    @SerializedName("wrongAnswers")
    private String wrongAnswersStr;
    @SerializedName("type")
    private String type;
    @SerializedName("difficulty")
    private String difficulty;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public List<String> getWrongAnswers() {
        List<String> list = new ArrayList<>();
        for(String answer:wrongAnswersStr.split(";")){
            list.add(answer);
        }
        return list;
    }

    public void setWrongAnswersStr(String wrongAnswersStr) {
        this.wrongAnswersStr = wrongAnswersStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}

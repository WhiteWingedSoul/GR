package com.hedspi.hoangviet.eslrecom.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoangviet on 11/21/16.
 */

public class MatchResult extends AdapterItem{
    private Material material;
    private double matchScore;
    private double kanseiScore;

    private double interestingScore;
    private List<KanseiKeyword> interestingItems;
//    private double satisfyScore;
//    private List<KanseiKeyword> satisfyItems;
    private double understandableScore;
    private List<KanseiKeyword> understandableItems;
    private double affordableScore;
    private KanseiKeyword affordableItem;

    private List<KanseiKeyword> kanseiItems;

    public List<KanseiKeyword> getKanseiItems() {
        return kanseiItems;
    }

    public void setKanseiItems(List<KanseiKeyword> kanseiItems) {
        this.kanseiItems = kanseiItems;
    }

    public List<KanseiKeyword> getInterestingItems() {
        return interestingItems;
    }

    public void setInterestingItems(List<KanseiKeyword> interestingItems) {
        this.interestingItems = interestingItems;
    }

//    public List<KanseiKeyword> getSatisfyItems() {
//        return satisfyItems;
//    }
//
//    public void setSatisfyItems(List<KanseiKeyword> satisfyItems) {
//        this.satisfyItems = satisfyItems;
//    }

    public List<KanseiKeyword> getUnderstandableItems() {
        return understandableItems;
    }

    public void setUnderstandableItems(List<KanseiKeyword> understandableItems) {
        this.understandableItems = understandableItems;
    }

    public KanseiKeyword getAffordableItem() {
        return affordableItem;
    }

    public void setAffordableItem(KanseiKeyword affordableItem) {
        this.affordableItem = affordableItem;
    }

    public double getInterestingScore() {
        return interestingScore;
    }

    public void setInterestingScore(double interestingScore) {
        this.interestingScore = interestingScore;
    }

//    public double getSatisfyScore() {
//        return satisfyScore;
//    }
//
//    public void setSatisfyScore(double satisfyScore) {
//        this.satisfyScore = satisfyScore;
//    }

    public double getUnderstandableScore() {
        return understandableScore;
    }

    public void setUnderstandableScore(double understandableScore) {
        this.understandableScore = understandableScore;
    }

    public double getAffordableScore() {
        return affordableScore;
    }

    public void setAffordableScore(double affordableScore) {
        this.affordableScore = affordableScore;
    }

    private List<String> positiveKeywords = new ArrayList<>();
    private List<String> negativeKeywords = new ArrayList<>();

    public List<String> getPositiveKeywords() {
        return positiveKeywords;
    }

    public List<String> getNegativeKeywords() {
        return negativeKeywords;
    }

    public double getKanseiScore() {
        return kanseiScore;
    }

    public void setKanseiScore(double kanseiScore) {
        this.kanseiScore = kanseiScore;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }


}

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

    private List<KanseiItem> kanseiMatchTags = new ArrayList<>();

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

    public List<KanseiItem> getLegitTags() {
        return kanseiMatchTags;
    }
}

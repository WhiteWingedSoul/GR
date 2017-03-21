package com.hedspi.hoangviet.eslrecom.models;

/**
 * Created by hoangviet on 11/21/16.
 */

public class MatchResult {
    private Material material;
    private double matchScore;

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

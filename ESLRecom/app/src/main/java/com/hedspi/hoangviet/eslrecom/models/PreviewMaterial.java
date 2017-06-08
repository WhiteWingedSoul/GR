package com.hedspi.hoangviet.eslrecom.models;

import java.io.Serializable;

/**
 * Created by viet on 6/7/17.
 */

public class PreviewMaterial implements Serializable{

    private String materialId;
    private String materialName;
    private String materialIconLink;
    private String materialDescription;
    private double contextScore;
    private double kanseiScore;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialIconLink() {
        return materialIconLink;
    }

    public void setMaterialIconLink(String materialIconLink) {
        this.materialIconLink = materialIconLink;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public double getContextScore() {
        return contextScore;
    }

    public void setContextScore(double contextScore) {
        this.contextScore = contextScore;
    }

    public double getKanseiScore() {
        return kanseiScore;
    }

    public void setKanseiScore(double kanseiScore) {
        this.kanseiScore = kanseiScore;
    }
}

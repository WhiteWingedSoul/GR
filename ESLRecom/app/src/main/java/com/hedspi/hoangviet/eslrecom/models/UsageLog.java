package com.hedspi.hoangviet.eslrecom.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by viet on 6/7/17.
 */

public class UsageLog implements Serializable{
    private String createdTime;
    private String uid;
    private String username;
    private String avatar;
    private String preferences;
    private List<PreviewMaterial> goodRatingMaterialIds = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public List<PreviewMaterial> getGoodRatingMaterialIds() {
        return goodRatingMaterialIds;
    }

    public void setGoodRatingMaterialIds(List<PreviewMaterial> goodRatingMaterialIds) {
        this.goodRatingMaterialIds = goodRatingMaterialIds;
    }
}

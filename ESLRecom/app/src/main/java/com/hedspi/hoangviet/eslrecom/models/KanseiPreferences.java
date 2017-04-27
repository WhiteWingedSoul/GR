package com.hedspi.hoangviet.eslrecom.models;

import java.util.List;

/**
 * Created by viet on 4/27/17.
 */

public class KanseiPreferences {
    //TODO calculate base on avarage number items ?
    // ???
    private List<KanseiItem> authors;
    private List<KanseiItem> sellerNames;
    private List<KanseiItem> sellerPrices;

    // MUST HAVE
    private List<KanseiItem> docTypes;
    private List<KanseiItem> tags;


    public List<KanseiItem> getAuthors() {
        return authors;
    }

    public void setAuthors(List<KanseiItem> authors) {
        this.authors = authors;
    }

    public List<KanseiItem> getSellerNames() {
        return sellerNames;
    }

    public void setSellerNames(List<KanseiItem> sellerNames) {
        this.sellerNames = sellerNames;
    }

    public List<KanseiItem> getSellerPrices() {
        return sellerPrices;
    }

    public void setSellerPrices(List<KanseiItem> sellerPrices) {
        this.sellerPrices = sellerPrices;
    }

    public List<KanseiItem> getDocTypes() {
        return docTypes;
    }

    public void setDocTypes(List<KanseiItem> docTypes) {
        this.docTypes = docTypes;
    }

    public List<KanseiItem> getTags() {
        return tags;
    }

    public void setTags(List<KanseiItem> tags) {
        this.tags = tags;
    }
}

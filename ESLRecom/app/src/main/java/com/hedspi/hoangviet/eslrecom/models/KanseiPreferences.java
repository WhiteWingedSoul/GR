package com.hedspi.hoangviet.eslrecom.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by viet on 4/27/17.
 */

public class KanseiPreferences {
    //TODO calculate base on avarage number items ?
    // ???
//    private List<KanseiItem> authors;
//    private List<KanseiItem> sellerNames;
//    private List<KanseiItem> sellerPrices;

    // MUST HAVE
    private List<KanseiItem> docTypes;
    private List<KanseiItem> tags;


//    public List<KanseiItem> getAuthors() {
//        return authors;
//    }
//
//    public void setAuthors(List<KanseiItem> authors) {
//        this.authors = authors;
//    }
//
//    public List<KanseiItem> getSellerNames() {
//        return sellerNames;
//    }
//
//    public void setSellerNames(List<KanseiItem> sellerNames) {
//        this.sellerNames = sellerNames;
//    }
//
//    public List<KanseiItem> getSellerPrices() {
//        return sellerPrices;
//    }
//
//    public void setSellerPrices(List<KanseiItem> sellerPrices) {
//        this.sellerPrices = sellerPrices;
//    }

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

    public KanseiItem retrieveDocType(String docTypeName){
        if (docTypes != null && docTypes.size() != 0){
            for (KanseiItem item : docTypes){
                if (item.getName().equals(docTypeName))
                    return item;
            }
        }
        return null;
    }

    public KanseiItem retrieveTag(String tagName){
        if (tags != null && tags.size() != 0){
            for (KanseiItem item : tags){
                if (item.getName().equals(tagName))
                    return item;
            }
        }
        return null;
    }

    public String retrieveKanseiTagResultTest(){
        String result = "";

        Collections.sort(tags, new Comparator<KanseiItem>() {
            @Override
            public int compare(KanseiItem result2, KanseiItem result1) {
                return Double.compare(result1.retrieveValue(), result2.retrieveValue());
            }
        });

        for(KanseiItem item:tags){
            if (item.getTotalTimeRated()>1 ) {
                String name = item.getName();
                String score = item.retrieveValue() > 0 ? "<font color='blue'>" + item.retrieveValue() + "</font>" : "<font color='red'>" + item.retrieveValue() + "</font>";
                String number = "" + item.getTotalTimeRated();
                result += name + " - " + score + " - " + number + "<br>";
            }
        }

        return result;
    }
}

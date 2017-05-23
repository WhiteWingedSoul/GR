package com.hedspi.hoangviet.eslrecom.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by viet on 4/27/17.
 */

public class KanseiPreferences {

    private KanseiKeyword interesting;
    private KanseiKeyword satisfy;
    private KanseiKeyword understandable;

    private KanseiKeyword acceptable;
    private KanseiKeyword affordable;

    public KanseiKeyword getInteresting() {
        return interesting;
    }

    public void setInteresting(KanseiKeyword interesting) {
        this.interesting = interesting;
    }

    public KanseiKeyword getSatisfy() {
        return satisfy;
    }

    public void setSatisfy(KanseiKeyword satisfy) {
        this.satisfy = satisfy;
    }

    public KanseiKeyword getUnderstandable() {
        return understandable;
    }

    public void setUnderstandable(KanseiKeyword understandable) {
        this.understandable = understandable;
    }

    public KanseiKeyword getAcceptable() {
        return acceptable;
    }

    public void setAcceptable(KanseiKeyword acceptable) {
        this.acceptable = acceptable;
    }

    public KanseiKeyword getAffordable() {
        return affordable;
    }

    public void setAffordable(KanseiKeyword affordable) {
        this.affordable = affordable;
    }

    //    //TODO calculate base on avarage number items ?
//    // ???
////    private List<KanseiKeyword> authors;
////    private List<KanseiKeyword> sellerNames;
////    private List<KanseiKeyword> sellerPrices;
//
//    // MUST HAVE
//    private List<KanseiKeyword> docTypes;
//    private List<KanseiKeyword> tags;
//
//
////    public List<KanseiKeyword> getAuthors() {
////        return authors;
////    }
////
////    public void setAuthors(List<KanseiKeyword> authors) {
////        this.authors = authors;
////    }
////
////    public List<KanseiKeyword> getSellerNames() {
////        return sellerNames;
////    }
////
////    public void setSellerNames(List<KanseiKeyword> sellerNames) {
////        this.sellerNames = sellerNames;
////    }
////
////    public List<KanseiKeyword> getSellerPrices() {
////        return sellerPrices;
////    }
////
////    public void setSellerPrices(List<KanseiKeyword> sellerPrices) {
////        this.sellerPrices = sellerPrices;
////    }
//
//    public List<KanseiKeyword> getDocTypes() {
//        return docTypes;
//    }
//
//    public void setDocTypes(List<KanseiKeyword> docTypes) {
//        this.docTypes = docTypes;
//    }
//
//    public List<KanseiKeyword> getTags() {
//        return tags;
//    }
//
//    public void setTags(List<KanseiKeyword> tags) {
//        this.tags = tags;
//    }
//
//    public KanseiKeyword retrieveDocType(String docTypeName){
//        if (docTypes != null && docTypes.size() != 0){
//            for (KanseiKeyword item : docTypes){
//                if (item.getName().equals(docTypeName))
//                    return item;
//            }
//        }
//        return null;
//    }
//
//    public KanseiKeyword retrieveTag(String tagName){
//        if (tags != null && tags.size() != 0){
//            for (KanseiKeyword item : tags){
//                if (item.getName().equals(tagName))
//                    return item;
//            }
//        }
//        return null;
//    }
//
//    public String retrieveKanseiTagResultTest(){
//        String result = "";
//
//        Collections.sort(tags, new Comparator<KanseiKeyword>() {
//            @Override
//            public int compare(KanseiKeyword result2, KanseiKeyword result1) {
//                return Double.compare(result1.retrieveValue(), result2.retrieveValue());
//            }
//        });
//
//        for(KanseiKeyword item:tags){
//            if (item.getTotalTimeRated()>1 ) {
//                String name = item.getName();
//                String score = item.retrieveValue() > 0 ? "<font color='blue'>" + item.retrieveValue() + "</font>" : "<font color='red'>" + item.retrieveValue() + "</font>";
//                String number = "" + item.getTotalTimeRated();
//                result += name + " - " + score + " - " + number + "<br>";
//            }
//        }
//
//        return result;
//    }
}

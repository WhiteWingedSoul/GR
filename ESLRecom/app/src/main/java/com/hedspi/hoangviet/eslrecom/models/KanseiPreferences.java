package com.hedspi.hoangviet.eslrecom.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by viet on 4/27/17.
 */

public class KanseiPreferences {
    public static final String TARGET_TAG = "tag";
    public static final String TARGET_NAME = "name";
    public static final String TARGET_PRICE = "price";

    private KanseiItem interesting;
    private KanseiItem satisfy;
    private KanseiItem understandable;

    private KanseiItem acceptable;
    private KanseiItem affordable;

    private List<KanseiItem> attributes;

    public void initAttributes(){
        for (KanseiItem attribute: allAttributes()){
            attribute.init();
        }
    }

    public List<KanseiItem> allAttributes(){
        if (attributes == null){
            attributes = new ArrayList<>();
            interesting = new KanseiItem();
            satisfy = new KanseiItem();
            understandable = new KanseiItem();
            acceptable = new KanseiItem();
            affordable = new KanseiItem();

            attributes.add(interesting);
            interesting.setTarget(TARGET_TAG);
            attributes.add(satisfy);
            satisfy.setTarget(TARGET_TAG);
            attributes.add(understandable);
            understandable.setTarget(TARGET_TAG);

            attributes.add(acceptable);
            acceptable.setTarget(TARGET_NAME);
            attributes.add(affordable);
            affordable.setTarget(TARGET_PRICE);
        }

        return attributes;
    }

    public double getAllWeights(){
        double weight = 0;

        for (KanseiItem attribute: allAttributes()){
            weight += attribute.retrieveWeight();
        }
        return weight;
    }

    public KanseiItem getInteresting() {
        return interesting;
    }

    public void setInteresting(KanseiItem interesting) {
        this.interesting = interesting;
    }

    public KanseiItem getSatisfy() {
        return satisfy;
    }

    public void setSatisfy(KanseiItem satisfy) {
        this.satisfy = satisfy;
    }

    public KanseiItem getUnderstandable() {
        return understandable;
    }

    public void setUnderstandable(KanseiItem understandable) {
        this.understandable = understandable;
    }

    public KanseiItem getAcceptable() {
        return acceptable;
    }

    public void setAcceptable(KanseiItem acceptable) {
        this.acceptable = acceptable;
    }

    public KanseiItem getAffordable() {
        return affordable;
    }

    public void setAffordable(KanseiItem affordable) {
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

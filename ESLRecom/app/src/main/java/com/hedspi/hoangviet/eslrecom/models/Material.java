package com.hedspi.hoangviet.eslrecom.models;

import com.google.gson.annotations.SerializedName;
import com.hedspi.hoangviet.eslrecom.managers.DatabaseManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by viet on 1/3/17.
 */

public class Material implements Serializable {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COVER_LINK = "coverLink";
    public static final String AUTHOR = "author";
    public static final String PUBLISHER = "publisher";
    public static final String EDITION_FORMAT = "editionformat";
    public static final String SUMMARY = "summary";
    public static final String SUBJECTS = "subjects";
    public static final String ABSTRACT = "abstract";
    public static final String CONTENT = "content";
    public static final String DESCRIPTION = "description";
    public static final String DOCTYPE = "docType";
    public static final String GENRE_FORM = "genreform";
    public static final String NOTE = "note";
    public static final String SELLER_LINK = "sellerLink";
    public static final String SELLER_NAME = "sellerName";
    public static final String SELLER_PRICE = "sellerPrice";
    public static final String ONLINE_NAME = "onlineName";
    public static final String ONLINE_LINK ="onlineLink";
    public static final String TAG = "tag";

    @SerializedName(NAME)
    private String name;
    @SerializedName(COVER_LINK)
    private String coverLink;
    @SerializedName(AUTHOR)
    private String author;
    @SerializedName(PUBLISHER)
    private String publisher;
    @SerializedName(EDITION_FORMAT)
    private String edition_format;
    @SerializedName(SUMMARY)
    private String summary;
    @SerializedName(SUBJECTS)
    private String subjects;
    @SerializedName(ABSTRACT)
    private String abstractString;
    @SerializedName(CONTENT)
    private String content;
    @SerializedName(DESCRIPTION)
    private String description;
    @SerializedName(DOCTYPE)
    private String documentType;
    @SerializedName(GENRE_FORM)
    private String genre_form;
    @SerializedName(NOTE)
    private String note;
    @SerializedName(SELLER_LINK)
    private String sellerLink;
    @SerializedName(SELLER_NAME)
    private String sellerName;
    @SerializedName(SELLER_PRICE)
    private String sellerPrice;
    @SerializedName(ONLINE_NAME)
    private String onlineName;
    @SerializedName(ONLINE_LINK)
    private String onlineLink;
    @SerializedName(TAG)
    private String tag;

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getEdition_format() {
        return edition_format;
    }

    public void setEdition_format(String edition_format) {
        this.edition_format = edition_format;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getAbstractString() {
        return abstractString;
    }

    public void setAbstractString(String abstractString) {
        this.abstractString = abstractString;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getGenre_form() {
        return genre_form;
    }

    public void setGenre_form(String genre_form) {
        this.genre_form = genre_form;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSellerLink() {
        return sellerLink;
    }

    public void setSellerLink(String sellerLink) {
        this.sellerLink = sellerLink;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPrice() {
        return sellerPrice;
    }

    public void setSellerPrice(String sellerPrice) {
        this.sellerPrice = sellerPrice;
    }

    public String getCoverLink() {
        return coverLink;
    }

    public void setCoverLink(String coverLink) {
        this.coverLink = coverLink;
    }

    public String getOnlineName() {
        return onlineName;
    }

    public void setOnlineName(String onlineName) {
        this.onlineName = onlineName;
    }

    public String getOnlineLink() {
        return onlineLink;
    }

    public void setOnlineLink(String onlineLink) {
        this.onlineLink = onlineLink;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getKeywordImportantScore(String keyword){
        List<Tag> tagList = DatabaseManager.getTagList();
        List<String> materialTagString = Arrays.asList(tag.trim().split(","));
        List<Tag> materialTagList = new ArrayList<>();
        if (tagList == null || tagList.size() == 0 || !tag.contains(keyword))
            return 0;
        else{
            double totalScore = 0;
            for (String tagString : materialTagString){
                boolean added = false;
                for (Tag tag : tagList){
                    if (tag.getName().equals(tagString)){
                        materialTagList.add(tag);
                        added = true;
                        totalScore += tag.getScore();
                        break;
                    }
                }
            }

            for(Tag tag : materialTagList){
                if (tag.getName().equals(keyword))
                    return tag.getScore()/totalScore;
            }

            return 0;
        }
    }
}

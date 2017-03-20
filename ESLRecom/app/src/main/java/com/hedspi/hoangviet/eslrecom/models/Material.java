package com.hedspi.hoangviet.eslrecom.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by viet on 1/3/17.
 */

@DatabaseTable(tableName = "materials")
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

    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = NAME)
    private String name;
    @DatabaseField(columnName = COVER_LINK)
    private String coverLink;
    @DatabaseField(columnName = AUTHOR)
    private String author;
    @DatabaseField(columnName = PUBLISHER)
    private String publisher;
    @DatabaseField(columnName = EDITION_FORMAT)
    private String edition_format;
    @DatabaseField(columnName = SUMMARY)
    private String summary;
    @DatabaseField(columnName = SUBJECTS)
    private String subjects;
    @DatabaseField(columnName = ABSTRACT)
    private String abstractString;
    @DatabaseField(columnName = CONTENT)
    private String content;
    @DatabaseField(columnName = DESCRIPTION)
    private String description;
    @DatabaseField(columnName = DOCTYPE)
    private String documentType;
    @DatabaseField(columnName = GENRE_FORM)
    private String genre_form;
    @DatabaseField(columnName = NOTE)
    private String note;
    @DatabaseField(columnName = SELLER_LINK)
    private String sellerLink;
    @DatabaseField(columnName = SELLER_NAME)
    private String sellerName;
    @DatabaseField(columnName = SELLER_PRICE)
    private String sellerPrice;
    @DatabaseField(columnName = ONLINE_NAME)
    private String onlineName;
    @DatabaseField(columnName = ONLINE_LINK)
    private String onlineLink;
    @DatabaseField(columnName = TAG)
    private String tag;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
}

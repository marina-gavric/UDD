package com.example.demop.model;

public class TextUnit {
    private long id;
    private String magazine;
    private String title;
    private String coauthors;
    private String keywords;
    private String content;
    private String scientificArea;
    private String author;

    public TextUnit(){}

    public TextUnit(String author, String magazine, String title, String coauthors, String keywords, String content, String scientificArea){
        this.magazine = magazine;
        this.setAuthor(author);
        this.title = title;
        this.coauthors = coauthors;
        this.keywords = keywords;
        this.content = content;
        this.scientificArea = scientificArea;
    }


    public TextUnit(long id,String author, String magazine, String title, String coauthors, String keywords, String content, String scientificArea){
        this.id = id;
        this.setAuthor(author);
        this.magazine = magazine;
        this.title = title;
        this.coauthors = coauthors;
        this.keywords = keywords;
        this.content = content;
        this.scientificArea = scientificArea;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMagazine() {
        return magazine;
    }

    public void setMagazine(String magazine) {
        this.magazine = magazine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoauthors() {
        return coauthors;
    }

    public void setCoauthors(String coauthors) {
        this.coauthors = coauthors;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getScientificArea() {
        return scientificArea;
    }

    public void setScientificArea(String scientificArea) {
        this.scientificArea = scientificArea;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

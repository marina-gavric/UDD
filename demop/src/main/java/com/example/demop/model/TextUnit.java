package com.example.demop.model;

public class TextUnit {
    private long id;
    private String magazine;
    private String title;
    private String keywords;
    private String content;
    private String scientificArea;
    private String authors;
    private String openAccess;

    public TextUnit(){
        this.openAccess="yes";
    }

    public TextUnit(String author, String magazine, String title, String keywords, String content, String scientificArea){
        this.magazine = magazine;
        this.setAuthors(author);
        this.title = title;
        this.keywords = keywords;
        this.content = content;
        this.setScientificArea(scientificArea);
    }


    public TextUnit(long id,String author, String magazine, String title, String keywords, String content, String scientificArea){
        this.id = id;
        this.setAuthors(author);
        this.magazine = magazine;
        this.title = title;
        this.keywords = keywords;
        this.content = content;
        this.setScientificArea(scientificArea);
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

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String author) {
        this.authors = author;
    }

    public String getOpenAccess() {
        return openAccess;
    }

    public void setOpenAccess(String openAccess) {
        this.openAccess = openAccess;
    }
}

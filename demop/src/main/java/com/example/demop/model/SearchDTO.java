package com.example.demop.model;

public class SearchDTO {
    private String field;
    private String operator;
    private String text;
    private String type;

    public SearchDTO(){}

    public SearchDTO(String field, String operator, String text, String type){
        this.field = field;
        this.operator = operator;
        this.text = text;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

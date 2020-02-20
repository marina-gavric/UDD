package com.example.demop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FormSubmissionDTO implements Serializable{
	String fieldId;
	String fieldValue;
	ArrayList<String> categories = new ArrayList<String>();
	
	public FormSubmissionDTO(){}
	
	public FormSubmissionDTO(String fieldId, String fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}
	
	
	public FormSubmissionDTO(String fieldId, String fieldValue,
			ArrayList<String> categories) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
		this.categories = categories;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	
}

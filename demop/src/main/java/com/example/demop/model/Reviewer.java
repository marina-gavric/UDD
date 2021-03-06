package com.example.demop.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reviewer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	public Reviewer(){
		
	}

	public Reviewer(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}

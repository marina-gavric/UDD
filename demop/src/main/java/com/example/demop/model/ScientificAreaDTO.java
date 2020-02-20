package com.example.demop.model;

public class ScientificAreaDTO {
	private Long id;
	private String name;

	public ScientificAreaDTO(){}

	public ScientificAreaDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

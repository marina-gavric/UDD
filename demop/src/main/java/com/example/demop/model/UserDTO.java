package com.example.demop.model;

public class UserDTO {
	private Long id;
	private String name;
	private String surname;
	private String role;
	private String username;

	public UserDTO(){}
	
	public UserDTO(Long id, String name, String surname) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
	}
	public UserDTO(User user){
		this.id = user.getId();
		this.username = user.getUsername();
		this.name= user.getName();
		this.surname = user.getSurname();

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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}


	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

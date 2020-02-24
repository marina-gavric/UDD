 package com.example.demop.model;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "city")
	private String city;

	@Column(name = "country")
	private String country;
	
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
	
	@Column(name = "mail")
	private String mail;

	@Column(name = "title")
	private String title;
	
	@Column(name="revFlag")
	private boolean reviewerFlag;
	
	@Column(name = "active")
	private boolean active;

	@Column(name = "payed")
	private boolean payed;

	@Column(name = "type")
	private String type;

	@Column(name = "longitude")
	private float longitude;

	@Column(name = "latitude")
	private float latitude;

	@ManyToMany(cascade =  {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "User_Roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<Role> roles = new ArrayList<>();

	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "User_Areas", 
	        joinColumns = { @JoinColumn(name = "user_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "scientificArea_id") }
	  )
	private Set<ScientificArea> interestedAreas = new HashSet<ScientificArea>();
	
	@ManyToMany(mappedBy = "reviewerMagazine")
	private Set<Magazine> reviewedMagazines = new HashSet<Magazine>();

	@ManyToMany(mappedBy = "coauthorText")
	private Set<Text> coauthoredTexts = new HashSet<Text>();

	@ManyToMany(mappedBy = "editorMagazine")
	private Set<Magazine> editedMagazines = new HashSet<Magazine>();

	@ManyToMany(mappedBy = "reviewersText")
	private Set<Text> reveiwedTexts = new HashSet<Text>();

	@OneToMany(mappedBy = "mainEditor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Magazine>  mainMagazines = new HashSet<Magazine>();

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Text>  texts = new HashSet<Text>();

	public User(){
		this.setActive(false);
		this.setType("user");
		
	}

	public User(Long id, String name, String surname, String city,
			String country, String username, String password, String mail,
			String title, boolean reviewerFlag, boolean active, String type) {
		super();
		this.setId(id);
		this.setName(name);
		this.setSurname(surname);
		this.setCity(city);
		this.setCountry(country);
		this.setUsername(username);
		this.setPassword(password);
		this.setMail(mail);
		this.setTitle(title);
		this.setReviewerFlag(reviewerFlag);
		this.setActive(active);
		this.setType(type);
	}

	public User(Long id, String name, String surname, String city,
			String country, String username, String password, String mail,
			String title, boolean reviewerFlag) {
		super();
		this.setId(id);
		this.setName(name);
		this.setSurname(surname);
		this.setCity(city);
		this.setCountry(country);
		this.setUsername(username);
		this.setPassword(password);
		this.setMail(mail);
		this.setTitle(title);
		this.setReviewerFlag(reviewerFlag);
	}

	public User(Long id, String name, String surname, String city,
			String country, String username, String password, String mail,
			String title, boolean reviewerFlag,
			Set<ScientificArea> scientificAreas) {
		super();
		this.setId(id);
		this.setName(name);
		this.setSurname(surname);
		this.setCity(city);
		this.setCountry(country);
		this.setUsername(username);
		this.setPassword(password);
		this.setMail(mail);
		this.setTitle(title);
		this.setReviewerFlag(reviewerFlag);
		this.setInterestedAreas(scientificAreas);
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



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<ScientificArea> getInterestedAreas() {
		return interestedAreas;
	}

	public void setInterestedAreas(Set<ScientificArea> interestedAreas) {
		this.interestedAreas = interestedAreas;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String getUsername() {
		return username;
	}

	 @Override
	 public boolean isAccountNonExpired() {
		 return true;
	 }

	 @Override
	 public boolean isAccountNonLocked() {
		 return true;
	 }

	 @Override
	 public boolean isCredentialsNonExpired() {
		 return true;
	 }

	 @Override
	 public boolean isEnabled() {
		 return true;
	 }

	 public void setUsername(String username) {
		this.username = username;
	}

	 @Override
	 public Collection<? extends GrantedAuthority> getAuthorities() {
		 if(!this.getRoles().isEmpty()){
			 Role r = getRoles().iterator().next();
			 List<Privilege> privileges = new ArrayList<Privilege>();
			 for(Privilege p : r.getPrivileges()){
				 privileges.add(p);
			 }
			 return privileges;
		 }
		 return null;

	 }
	@Override
	 public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isReviewerFlag() {
		return reviewerFlag;
	}

	public void setReviewerFlag(boolean reviewerFlag) {
		this.reviewerFlag = reviewerFlag;
	}

	public Set<ScientificArea> getScientificAreas() {
		return getInterestedAreas();
	}

	public void setScientificAreas(Set<ScientificArea> scientificAreas) {
		this.setInterestedAreas(scientificAreas);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Magazine> getReviewedMagazines() {
		return reviewedMagazines;
	}

	public void setReviewedMagazines(Set<Magazine> reviewedMagazines) {
		this.reviewedMagazines = reviewedMagazines;
	}

	public Set<Magazine> getEditedMagazines() {
		return editedMagazines;
	}

	public void setEditedMagazines(Set<Magazine> editedMagazines) {
		this.editedMagazines = editedMagazines;
	}


	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Set<Magazine> getMainMagazines() {
		return mainMagazines;
	}

	public void setMainMagazines(Set<Magazine> mainMagazines) {
		this.mainMagazines = mainMagazines;
	}

	public Set<Text> getTexts() {
		return texts;
	}

	public void setTexts(Set<Text> texts) {
		this.texts = texts;
	}

	public Set<Text> getCoauthoredTexts() {
		return coauthoredTexts;
	}

	public void setCoauthoredTexts(Set<Text> coauthoredTexts) {
		this.coauthoredTexts = coauthoredTexts;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public Set<Text> getReveiwedTexts() {
		return reveiwedTexts;
	}

	public void setReveiwedTexts(Set<Text> reveiwedTexts) {
		this.reveiwedTexts = reveiwedTexts;
	}
}

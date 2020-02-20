package com.example.demop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Magazine {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "issn", nullable = false)
	private String issn;
	
	@Column(name = "openaccess", nullable = false)
	private boolean openaccess;

	@Column(name = "active")
	private boolean active;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "Magazine_Areas", 
	        joinColumns = { @JoinColumn(name = "magazine_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "scientificArea_id") }
	  )
	private Set<ScientificArea> magazineAreas = new HashSet<ScientificArea>();
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "Magazine_Reviewer", 
	        joinColumns = { @JoinColumn(name = "magazine_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "user_id") }
	  )
	private Set<User> reviewerMagazine = new HashSet<User>();
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(
	        name = "Magazine_Editor", 
	        joinColumns = { @JoinColumn(name = "magazine_id") }, 
	        inverseJoinColumns = { @JoinColumn(name = "user_id") }
	  )
	private Set<User> editorMagazine = new HashSet<User>();


	@OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Text>  texts = new HashSet<Text>();

	@ManyToOne( fetch = FetchType.EAGER)
	private User mainEditor;

	public Magazine(){
		this.openaccess = false;
		this.active = false;
	}

	public Magazine(Long id) {
		super();
		this.id = id;
	}
	

	public Magazine(String title, String issn, boolean openaccess,
			Set<ScientificArea> magazineAreas) {
		super();
		this.title = title;
		this.issn = issn;
		this.openaccess = openaccess;
		this.magazineAreas = magazineAreas;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public boolean isOpenaccess() {
		return openaccess;
	}

	public void setOpenaccess(boolean openaccess) {
		this.openaccess = openaccess;
	}

	public Set<ScientificArea> getMagazineAreas() {
		return magazineAreas;
	}

	public void setMagazineAreas(Set<ScientificArea> magazineAreas) {
		this.magazineAreas = magazineAreas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<User> getReviewerMagazine() {
		return reviewerMagazine;
	}

	public void setReviewerMagazine(Set<User> reviewerMagazine) {
		this.reviewerMagazine = reviewerMagazine;
	}

	public Set<User> getEditorMagazine() {
		return editorMagazine;
	}

	public void setEditorMagazine(Set<User> editorMagazine) {
		this.editorMagazine = editorMagazine;
	}


	public Set<Text> getTexts() {
		return texts;
	}

	public void setTexts(Set<Text> texts) {
		this.texts = texts;
	}

	public User getMainEditor() {
		return mainEditor;
	}

	public void setMainEditor(User mainEditor) {
		this.mainEditor = mainEditor;
	}

}

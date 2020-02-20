package com.example.demop.model;

import java.util.*;

import javax.persistence.*;

import com.example.demop.services.ScientificAreaService;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "summary")
    private String summary;

    @Column(name = "pdf")
    private String pdf;

    @ManyToOne( fetch = FetchType.EAGER)
    private Magazine magazine;

    @ManyToOne( fetch = FetchType.EAGER)
    private User author;

    @ManyToOne( fetch = FetchType.EAGER)
    private ScientificArea scientificArea;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "Text_CoAuthor",
            joinColumns = { @JoinColumn(name = "text_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    private Set<User> coauthorText = new HashSet<User>();

    @OneToMany(mappedBy = "text", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Keyword>  keywords = new HashSet<Keyword>();

    public Text(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Magazine getMagazine() {
        return magazine;
    }

    public void setMagazine(Magazine magazine) {
        this.magazine = magazine;
    }

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }

    public ScientificArea getScientificArea() {
        return scientificArea;
    }

    public void setScientificArea(ScientificArea scientificArea) {
        this.scientificArea = scientificArea;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<User> getCoauthorText() {
        return coauthorText;
    }

    public void setCoauthorText(Set<User> coauthorText) {
        this.coauthorText = coauthorText;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
}

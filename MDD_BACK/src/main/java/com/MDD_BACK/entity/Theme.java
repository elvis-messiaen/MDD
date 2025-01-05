package com.MDD_BACK.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String title;

    @Column(unique = true)
    @NonNull
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Utilisateur author;

    @ManyToMany
    @JoinTable(
            name = "utilisateur_theme",
            joinColumns = @JoinColumn(name = "theme_id"),
            inverseJoinColumns = @JoinColumn(name = "utilisateur_id")
    )
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Article> articles = new HashSet<>();

    public Theme() {}

    public Theme(String title, String description, Utilisateur author, Set<Utilisateur> utilisateurs, Set<Article> articles) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.utilisateurs = utilisateurs;
        this.articles = articles;
    }

    public <E> Theme(String description, String title, Utilisateur utilisateur, HashSet<Utilisateur> utilisateurs) {
        this.description = description;
        this.title = title;
        this.author = utilisateur;
        this.utilisateurs = utilisateurs;
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public Utilisateur getAuthor() {
        return author;
    }

    public void setAuthor(Utilisateur author) {
        this.author = author;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public Set<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
        this.utilisateurs = utilisateurs;
    }
}

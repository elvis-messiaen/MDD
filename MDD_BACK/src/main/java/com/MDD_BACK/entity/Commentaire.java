package com.MDD_BACK.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String description;

    @NonNull
    private Date date;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Utilisateur author;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Commentaire() {
    }

    public Commentaire(@NonNull String description, @NonNull Date date, @NonNull Utilisateur author) {
        this.description = description;
        this.date = date;
        this.author = author;
    }

    public Commentaire(String description, Date date, Utilisateur utilisateur, Article article) {
        this.description = description;
        this.date = date;
        this.author = utilisateur;
        this.article = article;
    }

    @NonNull
    public Utilisateur getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull Utilisateur author) {
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
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
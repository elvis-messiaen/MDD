package com.MDD_BACK.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true, length = 100, nullable = false)
    private String title;

    @NonNull
    private Date date;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Utilisateur author;

    @NonNull
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "theme_id", nullable = false)
    private Theme theme;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentaire> commentaires;

    public Article() {
    }

    public Article(Utilisateur author, Date date, String description, String title) {
        this.author = author;
        this.date = date;
        this.description = description;
        this.title = title;
    }

    public Article(Long articleId) {
        this.id = articleId;
    }


    @NonNull
    public Utilisateur getAuthor() {
        return author;
    }

    public void setAuthor(@NonNull Utilisateur author) {
        this.author = author;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}

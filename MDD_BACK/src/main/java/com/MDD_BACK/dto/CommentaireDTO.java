package com.MDD_BACK.dto;

import java.time.LocalDate;
import java.util.Date;

public class CommentaireDTO {
    private Long id;
    private String description;
    private String authorUsername;
    private LocalDate date;
    private Long articleId;

    public CommentaireDTO() {
    }

    public CommentaireDTO(Long id, String description, String authorUsername, LocalDate date, Long articleId) {
        this.id = id;
        this.description = description;
        this.authorUsername = authorUsername;
        this.date = date;
        this.articleId = articleId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "CommentaireDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", authorUsername='" + authorUsername + '\'' +
                ", date=" + date +
                ", articleId=" + articleId +
                '}';
    }
}
package com.MDD_BACK.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class ArticleDTO {
    private Long id;
    private String title;
    private String description;
    private String authorUsername;
    private Long authorId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private List<CommentaireDTO> commentaires;
    private Long themeId;

    public ArticleDTO() {
    }

    public ArticleDTO(Long id, String title, String description, String authorUsername, LocalDate date, List<CommentaireDTO> commentaires, Long themeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorUsername = authorUsername;
        this.date = date;
        this.commentaires = commentaires;
        this.themeId = themeId;
    }

    public ArticleDTO(Long authorId, String authorUsername, List<CommentaireDTO> commentaires, LocalDate date, String description, Long id, Long themeId, String title) {
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.commentaires = commentaires;
        this.date = date;
        this.description = description;
        this.id = id;
        this.themeId = themeId;
        this.title = title;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CommentaireDTO> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<CommentaireDTO> commentaires) {
        this.commentaires = commentaires;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
package com.MDD_BACK.dto;

import java.util.Date;
import java.util.List;

public class ArticleDTO {
    private Long id;
    private String title;
    private String description;
    private String authorUsername;
    private Date date;
    private List<CommentaireDTO> commentaires;
    private Long themeId; // Ajout de la référence au thème

    public ArticleDTO() {
    }

    public ArticleDTO(Long id, String title, String description, String authorUsername, Date date, List<CommentaireDTO> commentaires, Long themeId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.authorUsername = authorUsername;
        this.date = date;
        this.commentaires = commentaires;
        this.themeId = themeId;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
}
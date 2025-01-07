package com.MDD_BACK.dto;

import java.time.LocalDate;
import java.util.List;

public class ArticleRequestDTO {
    private String title;
    private String description;
    private Long authorId;
    private LocalDate date;
    private Long themeId;
    private List<CommentaireDTO> commentaires;

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<CommentaireDTO> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<CommentaireDTO> commentaires) {
        this.commentaires = commentaires;
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

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

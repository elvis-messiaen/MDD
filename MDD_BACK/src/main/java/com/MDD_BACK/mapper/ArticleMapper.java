package com.MDD_BACK.mapper;

import com.MDD_BACK.dto.ArticleDTO;
import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Theme;
import com.MDD_BACK.entity.Utilisateur;

import java.time.LocalDate;

public class ArticleMapper {

    public static ArticleDTO toDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setDate(article.getDate());
        dto.setCommentaires(CommentaireMapper.toDTOList(article.getCommentaires()));
        dto.setThemeId(article.getTheme() != null ? article.getTheme().getId() : null);
        dto.setAuthorUsername(article.getAuthor() != null ? article.getAuthor().getUsername() : null);
        dto.setAuthorId(article.getAuthor() != null ? article.getAuthor().getId() : null);
        return dto;
    }

    public static Article toEntity(ArticleDTO dto) {
        Article article = new Article();
        article.setId(dto.getId());
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setDate( dto.getDate());

        article.setCommentaires(CommentaireMapper.toEntityList(dto.getCommentaires()));
        article.setTheme(new Theme(dto.getThemeId()));

        if (dto.getAuthorId() != null) {
            Utilisateur author = new Utilisateur();
            author.setId(dto.getAuthorId());
            article.setAuthor(author);
        }

        return article;
    }

}
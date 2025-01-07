package com.MDD_BACK.mapper;

import com.MDD_BACK.dto.CommentaireDTO;
import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.entity.Article;
import java.util.List;
import java.util.stream.Collectors;

public class CommentaireMapper {
    public static CommentaireDTO toDTO(Commentaire commentaire) {
        if (commentaire == null) {
            return null;
        }

        CommentaireDTO commentaireDTO = new CommentaireDTO();
        commentaireDTO.setId(commentaire.getId());
        commentaireDTO.setDescription(commentaire.getDescription());
        commentaireDTO.setDate(commentaire.getDate());
        commentaireDTO.setArticleId(commentaire.getArticle().getId());

        if (commentaire.getAuthor() != null) {
            commentaireDTO.setAuthorUsername(commentaire.getAuthor().getUsername());
        }

        return commentaireDTO;
    }

    public static Commentaire toEntity(CommentaireDTO commentaireDTO) {
        if (commentaireDTO == null) {
            return null;
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setId(commentaireDTO.getId());
        commentaire.setDescription(commentaireDTO.getDescription());
        commentaire.setDate(commentaireDTO.getDate());

        Article article = new Article(commentaireDTO.getArticleId());
        commentaire.setArticle(article);

        Utilisateur author = new Utilisateur();
        author.setUsername(commentaireDTO.getAuthorUsername());
        commentaire.setAuthor(author);

        return commentaire;
    }

    public static List<CommentaireDTO> toDTOList(List<Commentaire> commentaires) {
        if (commentaires == null) {
            return null;
        }
        return commentaires.stream()
                .map(CommentaireMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Commentaire> toEntityList(List<CommentaireDTO> commentaireDTOs) {
        if (commentaireDTOs == null) {
            return null;
        }
        return commentaireDTOs.stream()
                .map(CommentaireMapper::toEntity)
                .collect(Collectors.toList());
    }
}

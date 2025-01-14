package com.MDD_BACK.service;

import com.MDD_BACK.entity.Commentaire;

import java.util.List;
import java.util.Optional;

public interface ICommentaireService {

    Commentaire create(Commentaire commentaire, String authorUsername);

    List<Commentaire> findByArticleId(Long articleId);

    List<Commentaire> findAll();

    Commentaire update(Long id, Commentaire commentaire);

    void deleteById(Long id);
}
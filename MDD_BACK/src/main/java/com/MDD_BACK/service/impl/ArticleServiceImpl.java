package com.MDD_BACK.service.impl;

import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.ArticleRepository;
import com.MDD_BACK.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return (List<Article>) articleRepository.findAll();
    }

    @Override
    public Article create(Article article) {
        Utilisateur utilisateur = utilisateurService.getLoggedInUtilisateur();
        article.setAuthor(utilisateur);
        article.setDate(new Date());
        return articleRepository.save(article);
    }

    @Override
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public Article update(Long id, Article article) {
        if (articleRepository.existsById(id)) {
            article.setId(id);
            return articleRepository.save(article);
        }
        return null;
    }
}

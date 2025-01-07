package com.MDD_BACK.service.impl;

import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Theme;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.ArticleRepository;
import com.MDD_BACK.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleServiceImpl implements IArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @Autowired
    private ThemeServiceImpl themeService;

    @Override
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public List<Article> findAll() {
        return (List<Article>) articleRepository.findAll();
    }

    @Override
    @Transactional
    public Article create(Article article) {
        Utilisateur utilisateur = utilisateurService.getLoggedInUtilisateur();
        if (article.getTheme() == null || article.getTheme().getId() == null) {
            throw new IllegalArgumentException("Theme is null or Theme ID is null");
        }

        Theme theme = themeService.findById(article.getTheme().getId()).orElse(null);
        if (theme == null) {
            throw new IllegalArgumentException("Theme not found");
        }
        article.setTheme(theme);
        article.setAuthor(utilisateur);
        article.setDate(LocalDate.now());
        Article savedArticle = articleRepository.save(article);
        return savedArticle;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Article update(Long id, Article article) {
        if (articleRepository.existsById(id)) {
            article.setId(id);
            return articleRepository.save(article);
        }
        return null;
    }
}
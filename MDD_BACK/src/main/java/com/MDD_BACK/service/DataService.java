package com.MDD_BACK.service;

import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.entity.Theme;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.ArticleRepository;
import com.MDD_BACK.repository.CommentaireRepository;
import com.MDD_BACK.repository.ThemeRepository;
import com.MDD_BACK.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Service
public class DataService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Async
    public CompletableFuture<Void> sauvegarderUtilisateurs(List<Utilisateur> utilisateurs) {
        utilisateurs.forEach(utilisateur -> {
            if (!utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
                utilisateurRepository.save(utilisateur);
            }
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Article>> obtenirArticles() {
        List<Article> articles = (List<Article>) articleRepository.findAll();
        return CompletableFuture.completedFuture(articles);
    }

    @Async
    public CompletableFuture<List<Utilisateur>> obtenirUtilisateurs() {
        List<Utilisateur> utilisateurs = (List<Utilisateur>) utilisateurRepository.findAll();
        return CompletableFuture.completedFuture(utilisateurs);
    }

    @Async
    public CompletableFuture<Void> sauvegarderArticles(List<Article> articles) {
        articles.forEach(article -> {
            if (!articleRepository.existsByTitle(article.getTitle())) {
                articleRepository.save(article);
            }
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Theme>> obtenirThemes() {
        List<Theme> themes = (List<Theme>) themeRepository.findAll();
        return CompletableFuture.completedFuture(themes);
    }

    @Async
    @Transactional
    public CompletableFuture<Void> sauvegarderThemes(List<Theme> themes) {
        themeRepository.saveAll(themes);
        return CompletableFuture.completedFuture(null);
    }


    @Async
    @Transactional
    public CompletableFuture<Void> sauvegarderCommentaires(List<Commentaire> comments) {
        List<Commentaire> validComments = comments.stream()
                .filter(comment -> comment.getDescription() != null && !comment.getDescription().isEmpty())
                .collect(Collectors.toList());

        validComments.forEach(comment -> {
            try {
                commentaireRepository.save(comment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<List<Commentaire>> obtenirCommentaires() {
        List<Commentaire> comments = (List<Commentaire>) commentaireRepository.findAll();
        return CompletableFuture.completedFuture(comments);
    }
}

package com.MDD_BACK.controller;

import com.MDD_BACK.dto.ArticleDTO;
import com.MDD_BACK.dto.ArticleRequestDTO;
import com.MDD_BACK.dto.CommentaireDTO;
import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.entity.Theme;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.service.impl.ArticleServiceImpl;
import com.MDD_BACK.service.impl.CommentaireServiceImpl;
import com.MDD_BACK.service.impl.ThemeServiceImpl;
import com.MDD_BACK.service.impl.UtilisateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleServiceImpl articleService;

    @Autowired
    private ThemeServiceImpl themeService;

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @Autowired
    private CommentaireServiceImpl commentaireService;

    /**
     * Créer un nouvel article.
     *
     * @param articleRequest Les détails de l'article à créer.
     * @return L'article créé.
     */
    @Operation(summary = "Créer un nouvel article", description = "Crée un nouvel article basé sur les données fournies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Requête incorrecte", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleRequestDTO articleRequest) {
        Theme theme = themeService.findById(articleRequest.getThemeId()).orElse(null);
        if (theme == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Utilisateur author = utilisateurService.findById(articleRequest.getAuthorId()).orElse(null);
        if (author == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Article article = new Article();
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        article.setDate(articleRequest.getDate());
        article.setTheme(theme);
        article.setAuthor(author);
        article.setCommentaires(new ArrayList<>());

        try {
            Article savedArticle = articleService.create(article);
            ArticleDTO articleDTO = convertToDTO(savedArticle);
            return ResponseEntity.ok(articleDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtenir un article par ID.
     *
     * @param id L'ID de l'article à récupérer.
     * @return L'article correspondant à l'ID fourni.
     */
    @Operation(summary = "Obtenir un article par ID", description = "Récupère un article spécifique en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Article non trouvé", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);
        if (article.isPresent()) {
            ArticleDTO articleDTO = convertToDTO(article.get());
            List<CommentaireDTO> commentaires = commentaireService.findByArticleId(id).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            articleDTO.setCommentaires(commentaires);
            return ResponseEntity.ok(articleDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtenir tous les articles.
     *
     * @return Une liste de tous les articles disponibles.
     */
    @Operation(summary = "Obtenir tous les articles", description = "Récupère tous les articles disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            })
    })
    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = articleService.findAll();
        List<ArticleDTO> articleDTOS = articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        articleDTOS.forEach(articleDTO -> log.debug("Article ID: {}, Commentaires: {}", articleDTO.getId(), articleDTO.getCommentaires()));

        return ResponseEntity.ok(articleDTOS);
    }

    /**
     * Mettre à jour un article.
     *
     * @param id L'ID de l'article à mettre à jour.
     * @param articleRequest Les nouvelles données de l'article.
     * @return L'article mis à jour.
     */
    @Operation(summary = "Mettre à jour un article", description = "Met à jour un article existant en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Requête incorrecte", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "404", description = "Article non trouvé", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody ArticleRequestDTO articleRequest) {
        Theme theme = themeService.findById(articleRequest.getThemeId()).orElse(null);
        if (theme == null) {
            return ResponseEntity.badRequest().build();
        }

        Utilisateur author = utilisateurService.findById(articleRequest.getAuthorId()).orElse(null);
        if (author == null) {
            return ResponseEntity.badRequest().build();
        }

        Article article = new Article();
        article.setId(id);
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        article.setDate(articleRequest.getDate());
        article.setTheme(theme);
        article.setAuthor(author);

        Article updatedArticle = articleService.update(id, article);
        if (updatedArticle != null) {
            return ResponseEntity.ok(convertToDTO(updatedArticle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprimer un article.
     *
     * @param id L'ID de l'article à supprimer.
     * @return Une réponse sans contenu.
     */
    @Operation(summary = "Supprimer un article", description = "Supprime un article en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Convertir un article en DTO.
     *
     * @param article L'article à convertir.
     * @return L'ArticleDTO converti.
     */
    private ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setDescription(article.getDescription());
        articleDTO.setAuthorUsername(article.getAuthor().getUsername());
        articleDTO.setDate(article.getDate());
        articleDTO.setThemeId(article.getTheme().getId());
        return articleDTO;
    }

    /**
     * Convertir un commentaire en DTO.
     *
     * @param commentaire Le commentaire à convertir.
     * @return Le CommentaireDTO converti.
     */
    private CommentaireDTO convertToDTO(Commentaire commentaire) {
        CommentaireDTO commentaireDTO = new CommentaireDTO();
        commentaireDTO.setId(commentaire.getId());
        commentaireDTO.setDescription(commentaire.getDescription());
        commentaireDTO.setAuthorUsername(commentaire.getAuthor().getUsername());
        commentaireDTO.setDate(commentaire.getDate());
        commentaireDTO.setArticleId(commentaire.getArticle().getId());
        return commentaireDTO;
    }

}
package com.MDD_BACK.controller;

import com.MDD_BACK.dto.ArticleDTO;
import com.MDD_BACK.entity.Article;
import com.MDD_BACK.service.impl.ArticleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleServiceImpl articleService;

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
        Article savedArticle = articleService.create(article);
        ArticleDTO articleDTO = convertToDTO(savedArticle);
        return ResponseEntity.ok(articleDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.findById(id);
        return article.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = articleService.findAll();
        List<ArticleDTO> articleDTOS = articles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(articleDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article article) {
        Article updatedArticle = articleService.update(id, article);
        if (updatedArticle != null) {
            return ResponseEntity.ok(convertToDTO(updatedArticle));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

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
}

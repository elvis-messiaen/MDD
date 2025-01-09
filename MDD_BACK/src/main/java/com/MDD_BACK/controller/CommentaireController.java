package com.MDD_BACK.controller;

import com.MDD_BACK.dto.CommentaireDTO;
import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.service.impl.CommentaireServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {

    private static final Logger log = LoggerFactory.getLogger(CommentaireController.class);

    @Autowired
    private CommentaireServiceImpl commentaireService;

    @PostMapping
    public ResponseEntity<CommentaireDTO> createCommentaire(@RequestBody CommentaireDTO commentaireDTO, @RequestParam String authorUsername) {
        Commentaire commentaire = convertToEntity(commentaireDTO);
        Commentaire savedCommentaire = commentaireService.create(commentaire, authorUsername);
        CommentaireDTO savedCommentaireDTO = convertToDTO(savedCommentaire);
        return ResponseEntity.ok(savedCommentaireDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentaireDTO>> getCommentaireById(@PathVariable Long id) {
        List<Commentaire> commentaires = commentaireService.findByArticleId(id);
        List<CommentaireDTO> commentaireDTOs = commentaires.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentaireDTOs);
    }



    @GetMapping
    public ResponseEntity<List<CommentaireDTO>> getAllCommentaires() {
        List<Commentaire> commentaires = commentaireService.findAll();
        List<CommentaireDTO> commentaireDTOS = commentaires.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentaireDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentaireDTO> updateCommentaire(@PathVariable Long id, @RequestBody CommentaireDTO commentaireDTO) {
        Commentaire commentaire = convertToEntity(commentaireDTO);
        Commentaire updatedCommentaire = commentaireService.update(id, commentaire);
        if (updatedCommentaire != null) {
            return ResponseEntity.ok(convertToDTO(updatedCommentaire));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        commentaireService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CommentaireDTO convertToDTO(Commentaire commentaire) {
        CommentaireDTO commentaireDTO = new CommentaireDTO();
        commentaireDTO.setId(commentaire.getId());
        commentaireDTO.setDescription(commentaire.getDescription());
        commentaireDTO.setAuthorUsername(commentaire.getAuthor().getUsername());
        commentaireDTO.setDate(commentaire.getDate());
        commentaireDTO.setArticleId(commentaire.getArticle().getId());
        return commentaireDTO;
    }

    private Commentaire convertToEntity(CommentaireDTO commentaireDTO) {
        Commentaire commentaire = new Commentaire();
        commentaire.setId(commentaireDTO.getId());
        commentaire.setDescription(commentaireDTO.getDescription());
        commentaire.setAuthor(new Utilisateur(commentaireDTO.getAuthorUsername()));
        commentaire.setDate(commentaireDTO.getDate());
        commentaire.setArticle(new Article(commentaireDTO.getArticleId()));
        return commentaire;
    }
}
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {

    private static final Logger log = LoggerFactory.getLogger(CommentaireController.class);

    @Autowired
    private CommentaireServiceImpl commentaireService;

    @Operation(summary = "Créer un nouveau commentaire", description = "Crée un nouveau commentaire basé sur les données fournies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            })
    })
    @PostMapping
    public ResponseEntity<CommentaireDTO> createCommentaire(@RequestBody CommentaireDTO commentaireDTO, @RequestParam String authorUsername) {
        Commentaire commentaire = convertToEntity(commentaireDTO);
        Commentaire savedCommentaire = commentaireService.create(commentaire, authorUsername);
        CommentaireDTO savedCommentaireDTO = convertToDTO(savedCommentaire);
        return ResponseEntity.ok(savedCommentaireDTO);
    }

    @Operation(summary = "Obtenir les commentaires par ID d'article", description = "Récupère les commentaires d'un article spécifique en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Commentaire non trouvé", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<CommentaireDTO>> getCommentaireById(@PathVariable Long id) {
        List<Commentaire> commentaires = commentaireService.findByArticleId(id);
        List<CommentaireDTO> commentaireDTOs = commentaires.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentaireDTOs);
    }

    @Operation(summary = "Obtenir tous les commentaires", description = "Récupère tous les commentaires disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            })
    })
    @GetMapping
    public ResponseEntity<List<CommentaireDTO>> getAllCommentaires() {
        List<Commentaire> commentaires = commentaireService.findAll();
        List<CommentaireDTO> commentaireDTOS = commentaires.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentaireDTOS);
    }

    @Operation(summary = "Mettre à jour un commentaire", description = "Met à jour un commentaire existant en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Commentaire non trouvé", content = @io.swagger.v3.oas.annotations.media.Content)
    })
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

    @Operation(summary = "Supprimer un commentaire", description = "Supprime un commentaire en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu", content = @io.swagger.v3.oas.annotations.media.Content)
    })
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
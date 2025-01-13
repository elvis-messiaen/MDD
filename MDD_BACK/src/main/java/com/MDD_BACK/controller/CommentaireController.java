package com.MDD_BACK.controller;

import com.MDD_BACK.dto.CommentaireDTO;
import com.MDD_BACK.entity.Article;
import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.service.impl.CommentaireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {

    @Autowired
    private CommentaireServiceImpl commentaireService;

    /**
     * Créer un nouveau commentaire.
     *
     * @param commentaireDTO Les détails du commentaire à créer.
     * @param authorUsername Le nom d'utilisateur de l'auteur du commentaire.
     * @return Le commentaire créé.
     */
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

    /**
     * Obtenir les commentaires par ID d'article.
     *
     * @param id L'ID de l'article pour lequel récupérer les commentaires.
     * @return Les commentaires de l'article spécifié.
     */
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

    /**
     * Obtenir tous les commentaires.
     *
     * @return Une liste de tous les commentaires disponibles.
     */
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

    /**
     * Mettre à jour un commentaire.
     *
     * @param id L'ID du commentaire à mettre à jour.
     * @param commentaireDTO Les nouvelles données du commentaire.
     * @return Le commentaire mis à jour.
     */
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

    /**
     * Supprimer un commentaire.
     *
     * @param id L'ID du commentaire à supprimer.
     * @return Une réponse sans contenu.
     */
    @Operation(summary = "Supprimer un commentaire", description = "Supprime un commentaire en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentaire(@PathVariable Long id) {
        commentaireService.deleteById(id);
        return ResponseEntity.noContent().build();
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

    /**
     * Convertir un DTO en commentaire.
     *
     * @param commentaireDTO Le CommentaireDTO à convertir.
     * @return Le commentaire converti.
     */
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
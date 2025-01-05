package com.MDD_BACK.controller;

import com.MDD_BACK.dto.CommentaireDTO;
import com.MDD_BACK.entity.Commentaire;
import com.MDD_BACK.service.impl.CommentaireServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commentaire")
public class CommentaireController {

    @Autowired
    private CommentaireServiceImpl commentaireService;

    @PostMapping
    public ResponseEntity<CommentaireDTO> createCommentaire(@RequestBody Commentaire commentaire) {
        Commentaire savedCommentaire = commentaireService.create(commentaire);
        CommentaireDTO commentaireDTO = convertToDTO(savedCommentaire);
        return ResponseEntity.ok(commentaireDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentaireDTO> getCommentaireById(@PathVariable Long id) {
        Optional<Commentaire> commentaire = commentaireService.findById(id);
        return commentaire.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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
    public ResponseEntity<CommentaireDTO> updateCommentaire(@PathVariable Long id, @RequestBody Commentaire commentaire) {
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
        return commentaireDTO;
    }
}

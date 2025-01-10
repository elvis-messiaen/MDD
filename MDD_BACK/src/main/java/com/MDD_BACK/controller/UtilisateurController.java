package com.MDD_BACK.controller;

import com.MDD_BACK.dto.UtilisateurDTO;
import com.MDD_BACK.dto.UtilisateurResponseDTO;
import com.MDD_BACK.service.impl.UtilisateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @Operation(summary = "Obtenir tous les utilisateurs", description = "Récupère tous les utilisateurs disponibles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            })
    })
    @GetMapping
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurService.findAll();
    }

    @Operation(summary = "Obtenir un utilisateur par ID", description = "Récupère un utilisateur spécifique en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<Optional<UtilisateurResponseDTO>> getUtilisateurById(@PathVariable Long id) {
        Optional<UtilisateurResponseDTO> utilisateurResponseDTO = utilisateurService.getUserById(id);
        return ResponseEntity.ok(utilisateurResponseDTO);
    }

    @Operation(summary = "Créer un nouvel utilisateur", description = "Crée un nouvel utilisateur basé sur les données fournies.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            })
    })
    @PostMapping("/utilisateur")
    public UtilisateurDTO createUser(@RequestBody UtilisateurDTO userDTO) {
        return utilisateurService.createUtilisateur(userDTO);
    }

    @Operation(summary = "Mettre à jour un utilisateur", description = "Met à jour un utilisateur existant en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PutMapping("/utilisateur/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public UtilisateurDTO updateUser(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        return utilisateurService.updateUtilisateur(id, utilisateurDTO);
    }

    @Operation(summary = "Supprimer un utilisateur", description = "Supprime un utilisateur en utilisant son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aucun contenu", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUtilisateurById(@PathVariable Long id) {
        utilisateurService.deleteUtilisateurById(id);
    }
}
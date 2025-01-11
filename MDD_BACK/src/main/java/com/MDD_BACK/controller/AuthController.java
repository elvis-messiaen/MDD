package com.MDD_BACK.controller;

import com.MDD_BACK.dto.RegisterRequestDTO;
import com.MDD_BACK.dto.ResponseDTO;
import com.MDD_BACK.dto.UtilisateurResponseDTO;
import com.MDD_BACK.service.impl.UtilisateurAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.MDD_BACK.dto.AuthDTO.TokenResponse;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UtilisateurAuthService utilisateurAuthService;

    /**
     * Enregistrer un nouvel utilisateur.
     *
     * @param registerRequest Les détails d'enregistrement de l'utilisateur.
     * @return Une réponse avec un token d'authentification.
     */
    @Operation(summary = "Enregistrer un nouvel utilisateur", description = "Enregistre un nouvel utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Le nom d'utilisateur ou l'email est déjà pris.")
    })
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequest) {
        if (utilisateurAuthService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO("L'email est déjà pris"));
        }

        try {
            String token = utilisateurAuthService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO("Erreur interne"));
        }
    }

    /**
     * Déconnecter l'utilisateur.
     *
     * @return Une réponse indiquant que l'utilisateur a été déconnecté avec succès.
     */
    @Operation(summary = "Déconnecter l'utilisateur", description = "Déconnecte l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO> logout() {
        SecurityContextHolder.clearContext();
        ResponseDTO response = new ResponseDTO("Utilisateur déconnecté avec succès");
        return ResponseEntity.ok(response);
    }

    /**
     * Obtenir les informations de l'utilisateur authentifié.
     *
     * @return Les informations de l'utilisateur actuellement authentifié.
     */
    @Operation(summary = "Obtenir les informations de l'utilisateur authentifié", description = "Renvoie les informations de l'utilisateur actuellement authentifié.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'est pas trouvé.")
    })
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UtilisateurResponseDTO utilisateurResponseDTO = utilisateurAuthService.getUtilisateurInfo(username);
        if (utilisateurResponseDTO != null) {
            return ResponseEntity.ok(utilisateurResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
    }

    /**
     * Mettre à jour les informations de l'utilisateur.
     *
     * @param utilisateurResponseDTO Les nouvelles informations de l'utilisateur.
     * @return Une réponse indiquant le succès ou l'échec de la mise à jour.
     */
    @Operation(summary = "Mettre à jour les informations de l'utilisateur", description = "Met à jour les informations de l'utilisateur actuellement authentifié.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody UtilisateurResponseDTO utilisateurResponseDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean updateSuccess = utilisateurAuthService.updateUtilisateurInfo(username, utilisateurResponseDTO);
        if (updateSuccess) {
            return ResponseEntity.ok(new ResponseDTO("Profile updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
    }

    /**
     * Vérifie si l'email ou le nom d'utilisateur existe déjà.
     *
     * @param email L'email à vérifier.
     * @param username Le nom d'utilisateur à vérifier.
     * @return Une réponse indiquant la disponibilité de l'email ou du nom d'utilisateur.
     */
    @Operation(summary = "Vérifie si l'email existe", description = "Vérifie si l'email existe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'email est disponible"),
            @ApiResponse(responseCode = "409", description = "L'email est déjà pris")
    })
    @GetMapping("/exists/{email}/{username}")
    public ResponseEntity<?> checkEmailOrUsernameExists(@PathVariable String email, @PathVariable String username) {
        boolean emailExists = utilisateurAuthService.existsByEmail(email);
        boolean usernameExists = utilisateurAuthService.existsByUsername(username);

        if (emailExists || usernameExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", "Le nom d'utilisateur ou l'email est déjà pris"));
        } else {
            return ResponseEntity.ok(Collections.singletonMap("message", "Disponible"));
        }
    }
}
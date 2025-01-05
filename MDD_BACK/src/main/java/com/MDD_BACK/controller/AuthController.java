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

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UtilisateurAuthService utilisateurAuthService;

    @Operation(summary = "Enregistrer un nouvel utilisateur", description = "Enregistre un nouvel utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Le nom d'utilisateur ou l'email est déjà pris.", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequestDTO registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty() ||
                registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty() ||
                registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        try {
            String token = utilisateurAuthService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDTO("Conflict"));
        }
    }

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



}
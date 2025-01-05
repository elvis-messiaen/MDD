package com.MDD_BACK.security;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Requête de connexion")
public class LoginRequest {
    @Schema(description = "Nom d'utilisateur", example = "user1")
    @NotBlank
    private String username;

    @Schema(description = "Mot de passe", example = "user")
    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
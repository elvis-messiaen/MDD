package com.MDD_BACK.dto;

public class LoginResponseDTO {
    private String token;
    private String identifier;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, String identifier) {
        this.token = token;
        this.identifier = identifier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return identifier;
    }

    public void setUsername(String identifier) {
        this.identifier = identifier;
    }
}
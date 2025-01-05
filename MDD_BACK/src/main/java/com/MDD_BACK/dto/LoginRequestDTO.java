package com.MDD_BACK.dto;

public class LoginRequestDTO {
    private String identifier;
    private String password;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static record TokenResponse(String token) {
    }

    public static record ErrorResponse(String message) {
    }
}
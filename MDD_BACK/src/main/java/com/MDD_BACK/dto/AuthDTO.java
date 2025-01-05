package com.MDD_BACK.dto;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record Response(String message, String token) {
    }

    public record RegisterRequest(String username, String password, String email) {
    }

    public record TokenResponse(String token) {
    }

    public record ResponseDTO(String message, String token) {
    }
}
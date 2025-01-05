package com.MDD_BACK.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UtilisateurResponseDTO {
    private Long id;

    @JsonProperty("username")
    private String username;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
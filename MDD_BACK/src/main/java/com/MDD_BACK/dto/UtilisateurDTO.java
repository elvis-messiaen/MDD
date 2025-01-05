package com.MDD_BACK.dto;

import com.MDD_BACK.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;
import java.util.Set;

public class UtilisateurDTO {
    private Long id;

    @JsonProperty("name")
    private String username;

    private String email;

    @JsonProperty("password")
    private String password;

    private Set<RoleDTO> role;

    public UtilisateurDTO(){}

    public UtilisateurDTO(Long id, String username, String email, String password, Set<RoleDTO> role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RoleDTO> getRoles() {
        return role;
    }

    public void setRoles(Set<RoleDTO> role) {
        this.role = role;
    }

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

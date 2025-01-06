package com.MDD_BACK.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String username;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @NonNull
    private String password;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnore
    private Set<Role> role = new HashSet<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Commentaire> commentaires = new ArrayList<>();

    @ManyToMany(mappedBy = "utilisateurs")
    private Set<Theme> themes = new HashSet<>();

    public Utilisateur() {
    }

    public Utilisateur(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Utilisateur(List<Commentaire> commentaires, String email, Long id, @NonNull String password, @NonNull String username) {
        this.commentaires = commentaires;
        this.email = email;
        this.id = id;
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public Utilisateur(String authorUsername) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Set<Theme> getThemes() {
        return themes;
    }

    public void setThemes(Set<Theme> themes) {
        this.themes = themes;
    }
}

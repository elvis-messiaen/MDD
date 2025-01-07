package com.MDD_BACK.repository;

import com.MDD_BACK.entity.Role;
import com.MDD_BACK.entity.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UtilisateurRepository extends CrudRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByEmail(String email);

    @Query("SELECT (COUNT(u) > 0) FROM Utilisateur u WHERE u.email = :email")
    boolean existsByEmail(String email);

    Optional<Utilisateur> findByUsername(String username);

    @Query("SELECT (COUNT(u) > 0) FROM Utilisateur u WHERE u.username = :username")
    boolean existsByUsername(String username);

    @Query("SELECT r FROM Utilisateur u JOIN u.role r WHERE u.username = :username")
    Set<Role> findRolesByUsername(String username);

    Optional<Utilisateur> findByUsernameOrEmail(String username, String email);
}

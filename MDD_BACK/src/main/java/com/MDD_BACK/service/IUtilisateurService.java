package com.MDD_BACK.service;

import com.MDD_BACK.dto.UtilisateurDTO;
import com.MDD_BACK.dto.UtilisateurResponseDTO;
import com.MDD_BACK.entity.Utilisateur;

import java.util.List;
import java.util.Optional;

public interface IUtilisateurService {

    UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO utilisateurDTO);

    UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO);

    Optional<UtilisateurResponseDTO> getUserById(Long id);

    boolean hasRole(String username, String roleName);

    void deleteUtilisateurById(Long id);

    List<UtilisateurDTO> findAll();

    Utilisateur getLoggedInUtilisateur();

}

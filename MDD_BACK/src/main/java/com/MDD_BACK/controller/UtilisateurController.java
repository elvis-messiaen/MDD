package com.MDD_BACK.controller;

import com.MDD_BACK.dto.UtilisateurDTO;
import com.MDD_BACK.dto.UtilisateurResponseDTO;
import com.MDD_BACK.service.impl.UtilisateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UtilisateurController {

    @Autowired
    private UtilisateurServiceImpl utilisateurService;

    @GetMapping
    public List<UtilisateurDTO> getAllUtilisateurs() {
        return utilisateurService.findAll();
    }


    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<Optional<UtilisateurResponseDTO>> getUtilisateurById(@PathVariable Long id) {
        Optional<UtilisateurResponseDTO> utilisateurResponseDTO = utilisateurService.getUserById(id);
        return ResponseEntity.ok(utilisateurResponseDTO);
    }

    @PostMapping("/utilisateur")
    public UtilisateurDTO createUser(@RequestBody UtilisateurDTO userDTO) {
        return utilisateurService.createUtilisateur(userDTO);
    }


    @PutMapping("/utilisateur/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public UtilisateurDTO updateUser(@PathVariable Long id, @RequestBody UtilisateurDTO utilisateurDTO) {
        return utilisateurService.updateUtilisateur(id, utilisateurDTO);
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUtilisateurById(@PathVariable Long id) {
        utilisateurService.deleteUtilisateurById(id);
    }




}

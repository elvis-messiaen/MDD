package com.MDD_BACK.service.impl;

import com.MDD_BACK.dto.UtilisateurDTO;
import com.MDD_BACK.dto.UtilisateurResponseDTO;
import com.MDD_BACK.entity.Role;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.RoleRepository;
import com.MDD_BACK.repository.UtilisateurRepository;
import com.MDD_BACK.service.IUtilisateurService;
import jakarta.persistence.EntityManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UtilisateurServiceImpl implements IUtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ModelMapper modelMapper;

    public UtilisateurServiceImpl(UtilisateurRepository userRepository) {
        this.utilisateurRepository = userRepository;
    }

    public List<UtilisateurDTO> findAll() {
        return StreamSupport.stream(utilisateurRepository.findAll().spliterator(), false)
                .map(user -> modelMapper.map(user, UtilisateurDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Utilisateur getLoggedInUtilisateur() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public Optional<UtilisateurResponseDTO> getUserById(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        UtilisateurResponseDTO utilisateurResponseDTO = new UtilisateurResponseDTO();
        utilisateurResponseDTO.setUsername(utilisateur.getUsername());
        utilisateurResponseDTO.setEmail(utilisateur.getEmail());
        utilisateurResponseDTO.setId(utilisateur.getId());
        return Optional.of(utilisateurResponseDTO);
    }

    public UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(utilisateurDTO.getUsername());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(utilisateurDTO.getPassword()));

        Set<Role> roles = utilisateurDTO.getRoles().stream()
                .map(roleDTO -> roleRepository.findById(roleDTO.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found")))
                .collect(Collectors.toSet());
        utilisateur.setRole(roles);

        utilisateur = utilisateurRepository.save(utilisateur);
        return modelMapper.map(utilisateur, UtilisateurDTO.class);
    }

    public UtilisateurDTO updateUtilisateur(Long id, UtilisateurDTO utilisateurDTO) {
        if (utilisateurRepository.existsById(id)) {
            Utilisateur utilisateur = modelMapper.map(utilisateurDTO, Utilisateur.class);

            Set<Role> roles = utilisateurDTO.getRoles().stream()
                    .map(roleDTO -> {
                        return roleRepository.findById(roleDTO.getId())
                                .orElseThrow(() -> new RuntimeException("Role not found"));
                    }).collect(Collectors.toSet());
            utilisateur.setRole(roles);

            utilisateur.setPassword(passwordEncoder.encode(utilisateurDTO.getPassword()));
            utilisateur.setId(id);
            utilisateur = utilisateurRepository.save(utilisateur);

            return modelMapper.map(utilisateur, UtilisateurDTO.class);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void deleteUtilisateurById(Long id) {
        if (utilisateurRepository.existsById(id)) {
            Utilisateur userEntity = utilisateurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
    }

    public boolean hasRole(String username, String roleName) {
        String query = "SELECT COUNT(ur) FROM Utilisateur u JOIN u.role ur WHERE u.username = :userName AND ur.name = :roleName";

        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("userName", username)
                .setParameter("roleName", roleName)
                .getSingleResult();

        return count > 0;

    }

    public Optional<Utilisateur> findByUsername(String username) {
        return utilisateurRepository.findByUsername(username);
    }

    public Optional<Utilisateur> findById(Long author_id) {
        return utilisateurRepository.findById(author_id);
    }

}
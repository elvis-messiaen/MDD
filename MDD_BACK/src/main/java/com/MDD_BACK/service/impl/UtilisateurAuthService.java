package com.MDD_BACK.service.impl;


import com.MDD_BACK.dto.RegisterRequestDTO;
import com.MDD_BACK.dto.UtilisateurResponseDTO;
import com.MDD_BACK.entity.Role;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.RoleRepository;
import com.MDD_BACK.repository.UtilisateurRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
public class UtilisateurAuthService {

    private static final Logger log = LoggerFactory.getLogger(UtilisateurAuthService.class);

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Transactional
    public String register(RegisterRequestDTO utilisateurDTO) {
        if (utilisateurRepository.existsByUsername(utilisateurDTO.getUsername())) {
            log.error("Le nom d'utilisateur est déjà pris : {}", utilisateurDTO.getUsername());
            throw new RuntimeException("Le nom d'utilisateur est déjà pris");
        }

        if (utilisateurRepository.existsByEmail(utilisateurDTO.getEmail())) {
            log.error("L'email est déjà pris : {}", utilisateurDTO.getEmail());
            throw new RuntimeException("L'email est déjà pris");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(utilisateurDTO.getUsername());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        utilisateur.setPassword(passwordEncoder.encode(utilisateurDTO.getPassword()));

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        utilisateur.setRole(roles);
        utilisateurRepository.save(utilisateur);

        String token = generateToken(utilisateur);
        return token;
    }



    private String generateToken(Utilisateur utilisateur) {
        return Jwts.builder()
                .setSubject(utilisateur.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public UtilisateurResponseDTO getUtilisateurInfo(String username) {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Utilisateur user = userOptional.get();
            UtilisateurResponseDTO userResponse = new UtilisateurResponseDTO();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setEmail(user.getEmail());
            return userResponse;
        }
        return null;
    }

    public boolean updateUtilisateurInfo(String username, UtilisateurResponseDTO utilisateurResponseDTO) {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Utilisateur utilisateur = userOptional.get();
            utilisateur.setUsername(utilisateurResponseDTO.getUsername());
            utilisateur.setEmail(utilisateurResponseDTO.getEmail());
            utilisateurRepository.save(utilisateur);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return utilisateurRepository.existsByUsername(username);
    }
}

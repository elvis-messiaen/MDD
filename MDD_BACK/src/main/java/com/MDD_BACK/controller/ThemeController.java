package com.MDD_BACK.controller;

import com.MDD_BACK.dto.SubscriptionRequestDTO;
import com.MDD_BACK.dto.ThemeDTO;
import com.MDD_BACK.entity.Theme;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.ThemeRepository;
import com.MDD_BACK.repository.UtilisateurRepository;
import com.MDD_BACK.service.impl.ThemeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/theme")
public class ThemeController {

    @Autowired
    private ThemeServiceImpl themeService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @PostMapping
    public ResponseEntity<ThemeDTO> createTheme(@RequestBody Theme theme) {
        Theme savedTheme = themeService.create(theme);
        ThemeDTO themeDTO = convertToDTO(savedTheme);
        return ResponseEntity.ok(themeDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDTO> getThemeById(@PathVariable Long id) {
        Optional<Theme> theme = themeService.findById(id);
        return theme.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ThemeDTO>> getAllThemes() {
        List<Theme> themes = themeService.findAll();
        List<ThemeDTO> themeDTOS = themes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(themeDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThemeDTO> updateTheme(@PathVariable Long id, @RequestBody Theme theme) {
        Theme updatedTheme = themeService.update(id, theme);
        if (updatedTheme != null) {
            return ResponseEntity.ok(convertToDTO(updatedTheme));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ThemeDTO convertToDTO(Theme theme) {
        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setId(theme.getId());
        themeDTO.setTitle(theme.getTitle());
        themeDTO.setDescription(theme.getDescription());
        return themeDTO;
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToTheme(@PathVariable Long id, @RequestBody SubscriptionRequestDTO subscriptionRequestDTO) {
        Long utilisateurId = subscriptionRequestDTO.getId();

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        Optional<Theme> themeOptional = themeRepository.findById(id);

        if (!utilisateurOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Utilisateur non trouvé"));
        }

        if (!themeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Thème non trouvé"));
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        Theme theme = themeOptional.get();

        if (theme.getUtilisateurs().contains(utilisateur)) {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Utilisateur déjà abonné à ce thème"));
        }

        theme.getUtilisateurs().add(utilisateur);
        themeRepository.save(theme);

        return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Abonnement réussi"));
    }


    @PostMapping("/{id}/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribeFromTheme(@PathVariable Long id, @RequestBody SubscriptionRequestDTO subscriptionRequestDTO) {
        Long utilisateurId = subscriptionRequestDTO.getId();

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        Optional<Theme> themeOptional = themeRepository.findById(id);

        if (!utilisateurOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Utilisateur non trouvé"));
        }

        if (!themeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "Thème non trouvé"));
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        Theme theme = themeOptional.get();

        if (!theme.getUtilisateurs().contains(utilisateur)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "Utilisateur non abonné à ce thème"));
        }

        theme.getUtilisateurs().remove(utilisateur);
        themeRepository.save(theme);

        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("message", "Désabonnement réussi"));
    }

    @PostMapping("/{id}/isSubscribed")
    public ResponseEntity<Map<String, Boolean>> isSubscribed(@PathVariable Long id, @RequestBody SubscriptionRequestDTO subscriptionRequestDTO) {
        Long utilisateurId = subscriptionRequestDTO.getId();

        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        Optional<Theme> themeOptional = themeRepository.findById(id);

        if (!utilisateurOptional.isPresent() || !themeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("subscribed", false));
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        Theme theme = themeOptional.get();

        boolean isSubscribed = theme.getUtilisateurs().contains(utilisateur);
        return ResponseEntity.ok(Collections.singletonMap("subscribed", isSubscribed));
    }

}
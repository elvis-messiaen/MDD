package com.MDD_BACK.service.impl;

import com.MDD_BACK.entity.Theme;
import com.MDD_BACK.entity.Utilisateur;
import com.MDD_BACK.repository.ThemeRepository;
import com.MDD_BACK.repository.UtilisateurRepository;
import com.MDD_BACK.service.IThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ThemeServiceImpl implements IThemeService {

    private static final Logger logger = LoggerFactory.getLogger(ThemeServiceImpl.class);

    @Autowired
    private final ThemeRepository themeRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public Theme create(Theme theme) {
        return themeRepository.save(theme);
    }

    @Override
    public Theme update(Long id, Theme theme) {
        if (themeRepository.existsById(id)) {
            theme.setId(id);
            return themeRepository.save(theme);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    @Override
    public List<Theme> findAll() {
        return (List<Theme>) themeRepository.findAll();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        return themeRepository.findById(id);
    }

    public boolean subscribeToTheme(Long utilisateurId, Long themeId) {
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(utilisateurId);
        Optional<Theme> themeOptional = themeRepository.findById(themeId);

        if (!utilisateurOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.").hasBody();
        }

        if (!themeOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Thème non trouvé.").hasBody();
        }

        Utilisateur utilisateur = utilisateurOptional.get();
        Theme theme = themeOptional.get();

        if (theme.getUtilisateurs().contains(utilisateur)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("L'utilisateur est déjà abonné à ce thème.").hasBody();
        }

        theme.getUtilisateurs().add(utilisateur);
        themeRepository.save(theme);
        return ResponseEntity.status(HttpStatus.CREATED).body("Abonnement réussi.").hasBody();
    }

    public boolean isAlreadySubscribed(String utilisateurId, Long themeId) {
        logger.debug("Vérification de l'abonnement: utilisateurId={}, themeId={}", utilisateurId, themeId);
        Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(Long.valueOf(utilisateurId));
        Optional<Theme> themeOptional = themeRepository.findById(themeId);

        if (utilisateurOptional.isPresent() && themeOptional.isPresent()) {
            Utilisateur utilisateur = utilisateurOptional.get();
            Theme theme = themeOptional.get();
            boolean alreadySubscribed = theme.getUtilisateurs().contains(utilisateur);
            logger.debug("Déjà abonné: {}", alreadySubscribed);
            return alreadySubscribed;
        }
        logger.warn("Utilisateur ou thème non trouvé");
        return false;
    }



}

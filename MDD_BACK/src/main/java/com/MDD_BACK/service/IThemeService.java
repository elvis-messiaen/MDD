package com.MDD_BACK.service;

import com.MDD_BACK.entity.Theme;

import java.util.List;
import java.util.Optional;

public interface IThemeService {

    Theme create(Theme theme);

    Theme update(Long id, Theme theme);

    void deleteById(Long id);

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    boolean subscribeToTheme(Long utilisateurId, Long themeId);

}
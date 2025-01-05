package com.MDD_BACK.configuration;


import com.MDD_BACK.repository.UtilisateurRepository;
import com.MDD_BACK.security.auth.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        AuthUser utilisateur = utilisateurRepository
                .findByUsernameOrEmail(identifier, identifier)
                .map(AuthUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));

        return utilisateur;
    }

}

package com.MDD_BACK.security.auth;

import com.MDD_BACK.configuration.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * Génère un token JWT pour l'utilisateur authentifié.
     *
     * @param authentication l'objet d'authentification de l'utilisateur
     * @return un token JWT signé
     */
    public String generateToken(Authentication authentication) {

        String username = authentication.getName();

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Key key = Keys.hmacShaKeyFor(jwtConfig.getJwtSecret().getBytes());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getJwtExpirationMs());

        return Jwts.builder()
                .setSubject(username)
                .claim("scope", scope)
                .setIssuer("self")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
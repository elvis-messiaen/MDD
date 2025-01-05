package com.MDD_BACK.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "chatop.app")
public class JwtConfig {

    private String jwtSecret;

    private int jwtExpirationMs;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public void setJwtExpirationMs(int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
    }
}
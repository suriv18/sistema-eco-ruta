package pe.edu.unmsm.ciudadsana.shared.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties(
        String secret,
        long expirationMs,
        long refreshExpirationMs
) {}

package pe.edu.unmsm.ciudadsana.shared.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.security.model.AuthenticatedUser;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_TENANT_ID = "tenantId";
    private static final String CLAIM_ROLES = "roles";

    private final SecretKey signingKey;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtTokenProvider(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
        this.expirationMs = properties.expirationMs();
        this.refreshExpirationMs = properties.refreshExpirationMs();
    }

    public String generateAccessToken(AuthenticatedUser user) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(user.usuarioId().toString())
                .claim(CLAIM_TENANT_ID, user.tenantId().toString())
                .claim(CLAIM_ROLES, user.roles())
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(signingKey)
                .compact();
    }

    public String generateRefreshToken(UUID usuarioId) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(usuarioId.toString())
                .issuedAt(new Date(now))
                .expiration(new Date(now + refreshExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    public AuthenticatedUser parseToken(String token) {
        Claims claims = parseClaims(token);
        UUID usuarioId = UUID.fromString(claims.getSubject());
        UUID tenantId = UUID.fromString(claims.get(CLAIM_TENANT_ID, String.class));

        @SuppressWarnings("unchecked")
        Set<String> roles = Set.copyOf((java.util.List<String>) claims.get(CLAIM_ROLES, java.util.List.class));

        return new AuthenticatedUser(usuarioId, tenantId, claims.getSubject(), roles);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID extractUsuarioId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

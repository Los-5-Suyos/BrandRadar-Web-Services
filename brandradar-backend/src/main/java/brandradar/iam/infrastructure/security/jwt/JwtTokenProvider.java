package brandradar.iam.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component("iamJwtTokenProvider")
public class JwtTokenProvider {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * Genera un JWT con claims de email y userId, firmado con HS256.
     */
    public String generateToken(String email, Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Genera un refresh token con expiración de 7 días.
     */
    public String generateRefreshToken(String email, Long userId, Long sessionVersion) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 604_800_000L); // 7 días

        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("sessionVersion", sessionVersion)
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(signingKey)
                .compact();
    }

    /**
     * Valida si el token es correcto y no ha expirado.
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrae el email (subject) del token.
     */
    public String extractEmail(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extrae el userId del token.
     */
    public Long extractUserId(String token) {
        return parseClaims(token).get("userId", Long.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractSessionVersion(String token) {
        return parseClaims(token).get("sessionVersion", Long.class);
    }
}

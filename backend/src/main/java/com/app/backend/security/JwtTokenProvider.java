package com.app.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret:MySecretKeyForJWTTokenGenerationAndValidationPurposeOnly12345}")
    private String jwtSecret;

    @Value("${app.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 🔥 Generar token JWT
    public String generateToken(String userId) {
        SecretKey key = getSigningKey();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔥 Obtener userId desde un token
    public String getUserIdFromToken(String token) {
        SecretKey key = getSigningKey();

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload().getSubject();
    }

    // 🔥 Validar token
    public boolean validateToken(String token) {
        try {
            SecretKey key = getSigningKey();

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}

package com.myhomeledger.app.auth.jwt;

import com.myhomeledger.app.auth.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        byte[] keyBytes = properties.secret().getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(UUID userId, long sessionId) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(properties.accessTokenMinutes() * 60);
        return Jwts.builder()
                .subject(userId.toString())
                .claim("sid", sessionId)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(signingKey)
                .compact();
    }

    public ParsedAccessToken parseAccessToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        UUID userId = UUID.fromString(claims.getSubject());
        Long sid = claims.get("sid", Long.class);
        if (sid == null) {
            throw new IllegalArgumentException("Missing session id claim");
        }
        return new ParsedAccessToken(userId, sid);
    }

    public record ParsedAccessToken(UUID userId, long sessionId) {
    }
}

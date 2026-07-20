package com.personal.razorpay.merchant.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value(value = "${jwt.secret.key}")
    private String secretKey;

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String getAccessToken(String email, UUID merchantId, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(email)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60 * 100)))
                .claim("merchantId", merchantId)
                .claim("role", role)
                .signWith(getSecretKey())
                .compact();
    }

    public Claims verifyAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractRoles(Claims claims) {
        return claims.get("role", String.class);
    }

    public String getMerchantId(Claims claims) {
        return claims.get("merchantId", String.class);
    }
}


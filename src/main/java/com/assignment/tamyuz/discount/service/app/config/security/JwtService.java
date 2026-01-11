package com.assignment.tamyuz.discount.service.app.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String SECRET = "mysupersecretkeymysupersecretkey";
    private static final long EXPIRATION_MS = 3600000; // 1 hour

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("roles", List.of(role))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
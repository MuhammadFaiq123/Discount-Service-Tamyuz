package com.assignment.tamyuz.discount.service.app.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generateToken_shouldReturnValidJWT_withCorrectClaims() {
        String subject = "user@example.com";
        String role = "ROLE_USER";

        String token = jwtService.generateToken(subject, role);
        assertNotNull(token, "JWT token should not be null");

        // Decode token using the same secret key
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("mysupersecretkeymysupersecretkey".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(subject, claims.getSubject(), "JWT subject should match input");
        assertEquals(List.of(role), claims.get("roles", List.class), "JWT roles claim should match input");
        assertNotNull(claims.getIssuedAt(), "JWT issuedAt should not be null");
        assertNotNull(claims.getExpiration(), "JWT expiration should not be null");

        // Optional: verify expiration is roughly 1 hour later than issuedAt
        long diffMillis = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertTrue(diffMillis >= 3600000 - 1000 && diffMillis <= 3600000 + 1000, "JWT expiration should be ~1 hour");
    }
}


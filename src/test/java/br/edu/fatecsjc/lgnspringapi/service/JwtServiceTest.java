package br.edu.fatecsjc.lgnspringapi.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private final String secretKey = "bXlzZWNyZXRrZXlteXNlY3JldGtleW15c2VjcmV0a2V5bXlzZWNyZXRrZXk=";
    private final long jwtExpiration = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService.secretKey = secretKey;
        jwtService.jwtExpiration = jwtExpiration;
        jwtService.refreshExpiration = 1000 * 60 * 60 * 24 * 7;
    }

    @Test
    void testExtractUsername() {
        when(userDetails.getUsername()).thenReturn("testuser");
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }

    @Test
    void testGenerateToken() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void testGenerateRefreshToken() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtService.generateRefreshToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void testIsTokenValid() {
        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        String token = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - jwtExpiration - 1000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        boolean isExpired = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());

        assertTrue(isExpired);
    }

    private Key getSignInKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
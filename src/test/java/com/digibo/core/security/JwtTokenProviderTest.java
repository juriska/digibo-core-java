package com.digibo.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private static final String SECRET = "test-secret-key-that-is-at-least-256-bits-long-for-hs256";

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(SECRET, 3600000, 86400000);
    }

    @Test
    void generateToken_createsValidToken() {
        String token = tokenProvider.generateToken("testuser", "USER001", List.of("ROLE_USER"));

        assertNotNull(token);
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void getUsernameFromToken_returnsCorrectUsername() {
        String token = tokenProvider.generateToken("testuser", "USER001", List.of("ROLE_USER"));

        String username = tokenProvider.getUsernameFromToken(token);

        assertEquals("testuser", username);
    }

    @Test
    void getUserIdFromToken_returnsCorrectUserId() {
        String token = tokenProvider.generateToken("testuser", "USER001", List.of("ROLE_USER"));

        String userId = tokenProvider.getUserIdFromToken(token);

        assertEquals("USER001", userId);
    }

    @Test
    void getRolesFromToken_returnsCorrectRoles() {
        String token = tokenProvider.generateToken("testuser", "USER001", List.of("ROLE_USER", "ROLE_ADMIN"));

        List<String> roles = tokenProvider.getRolesFromToken(token);

        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    void validateToken_returnsFalseForInvalidToken() {
        assertFalse(tokenProvider.validateToken("invalid.token.here"));
    }

    @Test
    void validateToken_returnsFalseForEmptyToken() {
        assertFalse(tokenProvider.validateToken(""));
    }

    @Test
    void generateRefreshToken_createsRefreshToken() {
        String refreshToken = tokenProvider.generateRefreshToken("testuser");

        assertNotNull(refreshToken);
        assertTrue(tokenProvider.validateToken(refreshToken));
        assertTrue(tokenProvider.isRefreshToken(refreshToken));
    }

    @Test
    void isRefreshToken_returnsFalseForAccessToken() {
        String accessToken = tokenProvider.generateToken("testuser", "USER001", List.of("ROLE_USER"));

        assertFalse(tokenProvider.isRefreshToken(accessToken));
    }
}

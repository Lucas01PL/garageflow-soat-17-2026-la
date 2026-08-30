package br.com.fiap.tech.challeng.garageflow_soat_17_2026_la.shared.config.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "c2VjcmV0LWtleS1mb3ItZGV2ZWxvcG1lbnQtb25seS1jaGFuZ2UtaW4tcHJvZHVjdGlvbi1lbnZzLTI1Ng==";

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService(SECRET, 60);
    }

    @Test
    void shouldGenerateTokenWithEmailAndRoleClaims() {
        String token = jwtService.generateToken("admin@garageflow.com", "ADMIN");

        assertEquals("admin@garageflow.com", jwtService.extractEmail(token));
        assertEquals("ADMIN", jwtService.extractRole(token));
    }

    @Test
    void shouldConsiderFreshlyGeneratedTokenValid() {
        String token = jwtService.generateToken("admin@garageflow.com", "ADMIN");

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void shouldConsiderExpiredTokenInvalid() {
        JwtService expiringNowService = new JwtService(SECRET, 0);
        String token = expiringNowService.generateToken("admin@garageflow.com", "ADMIN");

        assertFalse(expiringNowService.isTokenValid(token));
    }

    @Test
    void shouldConsiderMalformedTokenInvalid() {
        assertFalse(jwtService.isTokenValid("not-a-valid-jwt"));
    }
}

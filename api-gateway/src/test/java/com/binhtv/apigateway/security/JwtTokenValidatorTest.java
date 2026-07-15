package com.binhtv.apigateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.binhtv.apigateway.configuration.GatewayJwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenValidatorTest {
    private static final String SECRET = "a-test-secret-that-is-long-enough";
    private JwtTokenValidator validator;

    @BeforeEach
    void setUp() {
        GatewayJwtProperties properties = new GatewayJwtProperties();
        properties.setSecretKey(SECRET);
        properties.setIssuer("evo-test");
        validator = new JwtTokenValidator(properties);
    }

    @Test
    void extractsTrustedIdentityClaims() {
        String token = token("account-123", Instant.now().plusSeconds(60));

        JwtPrincipal principal = validator.validate(token);

        assertThat(principal.accountId()).isEqualTo("account-123");
        assertThat(principal.email()).isEqualTo("user@evo.test");
        assertThat(principal.role()).isEqualTo("ADMIN");
    }

    @Test
    void rejectsTokenWithoutAccountId() {
        String token = JWT.create()
                .withIssuer("evo-test")
                .withSubject("user@evo.test")
                .withExpiresAt(Date.from(Instant.now().plusSeconds(60)))
                .sign(Algorithm.HMAC256(SECRET));

        assertThatThrownBy(() -> validator.validate(token)).isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void rejectsExpiredToken() {
        assertThatThrownBy(() -> validator.validate(token("account-123", Instant.now().minusSeconds(1))))
                .isInstanceOf(JWTVerificationException.class);
    }

    private String token(String accountId, Instant expiresAt) {
        return JWT.create()
                .withIssuer("evo-test")
                .withSubject("user@evo.test")
                .withClaim("accountId", accountId)
                .withClaim("role", "ADMIN")
                .withExpiresAt(Date.from(expiresAt))
                .sign(Algorithm.HMAC256(SECRET));
    }
}

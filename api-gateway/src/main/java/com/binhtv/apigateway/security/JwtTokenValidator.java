package com.binhtv.apigateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.binhtv.apigateway.configuration.GatewayJwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenValidator {
    private static final String ROLE_CLAIM = "role";

    private final GatewayJwtProperties jwtProperties;

    public JwtPrincipal validate(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes());
        Verification verification = JWT.require(algorithm);

        if (jwtProperties.getIssuer() != null && !jwtProperties.getIssuer().isBlank()) {
            verification.withIssuer(jwtProperties.getIssuer());
        }

        JWTVerifier verifier = verification.build();
        DecodedJWT decodedJWT = verifier.verify(token);

        return new JwtPrincipal(
                decodedJWT.getSubject(),
                decodedJWT.getClaim(ROLE_CLAIM).asString());
    }
}
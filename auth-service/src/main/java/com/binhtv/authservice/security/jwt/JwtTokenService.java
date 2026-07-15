package com.binhtv.authservice.security.jwt;

import com.binhtv.authservice.dto.LoginRequest;
import com.binhtv.authservice.dto.LoginResponse;
import com.binhtv.authservice.model.User;
import com.binhtv.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService authService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {

        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        authenticationManager.authenticate(authenticationToken);

        final User user = authService.findByEmail(email);
        return createLoginResponse(user);
    }

    public LoginResponse createLoginResponse(User user) {
        final String token = jwtTokenManager.generateToken(user);
        final Date tokenExpiration = jwtTokenManager.getExpirationDateFromToken(token);
        final Instant expiresAt = tokenExpiration.toInstant();

        log.info("{} has successfully logged in!", user.getEmail());

        return new LoginResponse(token, expiresAt);
    }

}

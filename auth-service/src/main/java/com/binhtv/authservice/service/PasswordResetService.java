package com.binhtv.authservice.service;

import com.binhtv.authservice.event.AuthEventProducer;
import com.binhtv.authservice.event.PasswordResetRequestedEvent;
import com.binhtv.authservice.model.PasswordResetToken;
import com.binhtv.authservice.model.User;
import com.binhtv.authservice.repository.PasswordResetTokenRepository;
import com.binhtv.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private static final Duration TOKEN_TTL = Duration.ofMinutes(15);

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthEventProducer eventProducer;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Transactional
    public void requestReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return;
        }

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        tokenRepository.save(new PasswordResetToken(user, hash(rawToken), Instant.now().plus(TOKEN_TTL)));
        eventProducer.passwordResetRequested(new PasswordResetRequestedEvent(
                user.getEmail(), user.getUsername(), frontendUrl + "/dat-lai-mat-khau?token=" + rawToken));
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        PasswordResetToken token = tokenRepository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new IllegalArgumentException("Reset token is invalid or expired."));
        if (token.getUsedAt() != null || token.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Reset token is invalid or expired.");
        }

        token.getUser().setPassword(passwordEncoder.encode(newPassword));
        token.setUsedAt(Instant.now());
    }

    private String hash(String value) {
        try {
            return Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable.", exception);
        }
    }
}

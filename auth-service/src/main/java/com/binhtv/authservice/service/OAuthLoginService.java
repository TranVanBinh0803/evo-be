package com.binhtv.authservice.service;

import com.binhtv.authservice.dto.LoginResponse;
import com.binhtv.authservice.event.UserRegisteredEvent;
import com.binhtv.authservice.event.UserRegisteredProducer;
import com.binhtv.authservice.model.AuthProvider;
import com.binhtv.authservice.model.OAuthAccount;
import com.binhtv.authservice.model.OAuthLoginCode;
import com.binhtv.authservice.model.User;
import com.binhtv.authservice.model.UserRole;
import com.binhtv.authservice.repository.OAuthAccountRepository;
import com.binhtv.authservice.repository.OAuthLoginCodeRepository;
import com.binhtv.authservice.repository.UserRepository;
import com.binhtv.authservice.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final OAuthAccountRepository accountRepository;
    private final OAuthLoginCodeRepository codeRepository;
    private final UserRepository userRepository;
    private final UserRegisteredProducer userRegisteredProducer;
    private final JwtTokenService jwtTokenService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public String completeProviderLogin(AuthProvider provider, String subject, String email, String displayName) {
        User user = accountRepository.findByProviderAndProviderSubject(provider, subject)
                .map(OAuthAccount::getUser)
                .orElseGet(() -> createProviderAccount(provider, subject, email, displayName));

        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        String rawCode = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        codeRepository.save(new OAuthLoginCode(hash(rawCode), Instant.now().plusSeconds(60), user));
        return rawCode;
    }

    @Transactional
    public LoginResponse exchange(String rawCode) {
        OAuthLoginCode code = codeRepository.findByCodeHash(hash(rawCode))
                .orElseThrow(() -> new IllegalArgumentException("OAuth login code is invalid or expired."));
        if (code.getUsedAt() != null || code.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("OAuth login code is invalid or expired.");
        }
        code.setUsedAt(Instant.now());
        return jwtTokenService.createLoginResponse(code.getUser());
    }

    private User createProviderAccount(AuthProvider provider, String subject, String email, String displayName) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("The social provider did not return a verified email.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account with this email already exists. Sign in with your password.");
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(displayName == null || displayName.isBlank() ? email.substring(0, email.indexOf('@')) : displayName);
        user.setUserRole(UserRole.USER);
        user = userRepository.save(user);
        accountRepository.save(new OAuthAccount(provider, subject, user));
        userRegisteredProducer.publish(new UserRegisteredEvent(user.getId(), user.getEmail(), user.getUsername()));
        return user;
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

package com.binhtv.authservice.security.oauth;

import com.binhtv.authservice.model.AuthProvider;
import com.binhtv.authservice.service.OAuthLoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final OAuthLoginService loginService;

    @Value("${app.frontend-url:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            OAuth2User principal = token.getPrincipal();
            AuthProvider provider = AuthProvider.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());
            String email = principal.getAttribute("email");
            if (provider == AuthProvider.GOOGLE && !Boolean.TRUE.equals(principal.getAttribute("email_verified"))) {
                throw new IllegalArgumentException("Google email is not verified.");
            }
            String code = loginService.completeProviderLogin(
                    provider, principal.getName(), email, principal.getAttribute("name"));
            response.sendRedirect(frontendUrl + "/oauth/callback?code=" + URLEncoder.encode(code, StandardCharsets.UTF_8));
        } catch (RuntimeException exception) {
            response.sendRedirect(frontendUrl + "/oauth/callback?error="
                    + URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8));
        }
    }
}

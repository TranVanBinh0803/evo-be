package com.binhtv.apigateway.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.binhtv.apigateway.configuration.GatewayJwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationGlobalFilter implements GlobalFilter, Ordered {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHENTICATED_USER_HEADER = "X-Authenticated-User";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    private final GatewayJwtProperties jwtProperties;
    private final JwtTokenValidator jwtTokenValidator;
    private final GatewayResponseWriter responseWriter;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isPublicRequest(request)) {
            return chain.filter(exchange);
        }

        String authorizationHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return responseWriter.write(
                    exchange.getResponse(),
                    HttpStatus.UNAUTHORIZED,
                    "Authorization header is missing or invalid.");
        }

        try {
            String token = authorizationHeader.substring(BEARER_PREFIX.length());
            JwtPrincipal principal = jwtTokenValidator.validate(token);
            ServerHttpRequest authenticatedRequest = request.mutate()
                    .header(AUTHENTICATED_USER_HEADER, principal.email())
                    .header(USER_ROLE_HEADER, principal.role() == null ? "" : principal.role())
                    .build();

            return chain.filter(exchange.mutate().request(authenticatedRequest).build());
        } catch (JWTVerificationException exception) {
            return responseWriter.write(
                    exchange.getResponse(),
                    HttpStatus.UNAUTHORIZED,
                    "JWT token is invalid or expired.");
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private boolean isPublicRequest(ServerHttpRequest request) {
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return true;
        }

        String path = request.getURI().getPath();
        return jwtProperties.getPublicPaths().stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
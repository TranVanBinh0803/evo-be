package com.binhtv.apigateway.security;

public record JwtPrincipal(String accountId, String email, String role) {
}

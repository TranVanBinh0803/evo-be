package com.binhtv.apigateway.security;

public record JwtPrincipal(String email, String role) {
}
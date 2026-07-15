package com.binhtv.authservice.event;

public record PasswordResetRequestedEvent(String email, String username, String resetUrl) {
}

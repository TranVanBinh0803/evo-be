package com.binhtv.sendsmsservice.service.consumer;

import com.binhtv.sendsmsservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "auth.password-reset.requested", groupId = "notification-service")
    public void passwordReset(String payload) throws JacksonException {
        PasswordResetEvent event = objectMapper.readValue(payload, PasswordResetEvent.class);
        emailService.sendPasswordReset(event.email(), event.username(), event.resetUrl());
    }

    @KafkaListener(topics = "order.placed", groupId = "notification-service")
    public void orderPlaced(String payload) throws JacksonException {
        OrderPlacedEvent event = objectMapper.readValue(payload, OrderPlacedEvent.class);
        emailService.sendOrderConfirmation(event.email(), event.receiverName(), event.orderNumber());
    }

    private record PasswordResetEvent(String email, String username, String resetUrl) {}
    private record OrderPlacedEvent(String email, String receiverName, String orderNumber) {}
}

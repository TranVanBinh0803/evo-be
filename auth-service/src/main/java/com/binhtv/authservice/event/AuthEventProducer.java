package com.binhtv.authservice.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class AuthEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void passwordResetRequested(PasswordResetRequestedEvent event) {
        try {
            kafkaTemplate.send("auth.password-reset.requested", event.email(), objectMapper.writeValueAsString(event));
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize password reset event.", exception);
        }
    }
}

package com.binhtv.authservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredProducer {

	private static final String USER_REGISTERED_TOPIC = "user.registered";

	private final ObjectMapper objectMapper;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void publish(UserRegisteredEvent event) {

		try {
			final String payload = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(USER_REGISTERED_TOPIC, event.getAccountId().toString(), payload);
			log.info("Published user registered event {}", payload);
		} catch (JacksonException exception) {
			throw new IllegalStateException("Could not serialize user registered event", exception);
		}
	}
}

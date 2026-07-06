package com.binhtv.userservice.consumer;

import com.binhtv.userservice.dto.UserRegisteredEvent;
import com.binhtv.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegisteredConsumer {

	private final ObjectMapper objectMapper;

	private final UserProfileService userProfileService;

	@KafkaListener(topics = "user.registered", groupId = "user-service-id")
	public void handleUserRegistered(String message) throws JacksonException {

		log.info("User registered event consumed {}", message);

		final UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
		userProfileService.createFrom(event);
	}
}

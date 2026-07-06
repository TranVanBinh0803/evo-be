package com.binhtv.userservice.controller;

import com.binhtv.userservice.model.UserProfile;
import com.binhtv.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserProfileController {

	private final UserProfileService userProfileService;

	@GetMapping
	public ResponseEntity<List<UserProfile>> getUserProfiles() {

		return new ResponseEntity<>(userProfileService.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<UserProfile> getByAccountId(@PathVariable UUID accountId) {

		final UserProfile userProfile = userProfileService.findByAccountId(accountId);
		return new ResponseEntity<>(userProfile, userProfile == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
}

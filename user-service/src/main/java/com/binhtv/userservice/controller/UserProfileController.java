package com.binhtv.userservice.controller;

import com.binhtv.userservice.model.UserProfile;
import com.binhtv.userservice.dto.ApiResponse;
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
	public ResponseEntity<ApiResponse<List<UserProfile>>> getUserProfiles() {

		return ResponseEntity.ok(new ApiResponse<>("Get users successful!", 200, userProfileService.findAll()));
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<ApiResponse<UserProfile>> getByAccountId(@PathVariable UUID accountId) {

		final UserProfile userProfile = userProfileService.findByAccountId(accountId);
		return new ResponseEntity<>(new ApiResponse<>(userProfile == null ? "User not found." : "Get user successful!",
				userProfile == null ? 404 : 200, userProfile), userProfile == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
}

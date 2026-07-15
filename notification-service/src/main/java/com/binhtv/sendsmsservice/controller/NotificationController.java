package com.binhtv.sendsmsservice.controller;

import com.binhtv.sendsmsservice.dto.ApiResponse;
import com.binhtv.sendsmsservice.dto.ContactRequest;
import com.binhtv.sendsmsservice.dto.NewsletterRequest;
import com.binhtv.sendsmsservice.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/api/contact")
    public ResponseEntity<ApiResponse<Void>> contact(@Valid @RequestBody ContactRequest request) {
        emailService.sendContact(request);
        return ResponseEntity.accepted().body(new ApiResponse<>("Contact request sent successfully!", 202, null));
    }

    @PostMapping("/api/newsletter/subscriptions")
    public ResponseEntity<ApiResponse<Void>> subscribe(@Valid @RequestBody NewsletterRequest request) {
        emailService.subscribe(request.email());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Newsletter subscription successful!", 201, null));
    }
}

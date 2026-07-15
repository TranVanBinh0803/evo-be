package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.PageResponse;
import com.binhtv.productservice.model.dto.ReviewRequestDto;
import com.binhtv.productservice.model.dto.ReviewResponseDto;
import com.binhtv.productservice.model.dto.support.ApiResponses;
import com.binhtv.productservice.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponseDto>>> getReviews(
            @PathVariable UUID productId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponses.ok("Get product reviews successful!", reviewService.getReviews(
                productId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> create(
            @PathVariable UUID productId,
            @RequestHeader("X-Authenticated-User-Id") UUID accountId,
            @RequestHeader("X-Authenticated-User") String reviewerName,
            @Valid @RequestBody ReviewRequestDto request) {
        return ApiResponses.created("Create review successful!", reviewService.create(productId, accountId, reviewerName, request));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> update(
            @PathVariable UUID productId,
            @RequestHeader("X-Authenticated-User-Id") UUID accountId,
            @Valid @RequestBody ReviewRequestDto request) {
        return ApiResponses.ok("Update review successful!", reviewService.update(productId, accountId, request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID productId,
            @RequestHeader("X-Authenticated-User-Id") UUID accountId) {
        reviewService.delete(productId, accountId);
        return ApiResponses.ok("Delete review successful!", null);
    }
}

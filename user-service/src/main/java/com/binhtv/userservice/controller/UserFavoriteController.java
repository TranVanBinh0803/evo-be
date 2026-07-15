package com.binhtv.userservice.controller;

import com.binhtv.userservice.dto.ApiResponse;
import com.binhtv.userservice.service.UserFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/favorites")
@RequiredArgsConstructor
public class UserFavoriteController {
    private final UserFavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UUID>>> getFavorites(
            @RequestHeader("X-Authenticated-User-Id") UUID accountId) {
        return ResponseEntity.ok(new ApiResponse<>("Get favorites successful!", 200,
                favoriteService.getProductIds(accountId)));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> add(@RequestHeader("X-Authenticated-User-Id") UUID accountId,
            @PathVariable UUID productId) {
        favoriteService.add(accountId, productId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product added to favorites!", 201, null));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> remove(@RequestHeader("X-Authenticated-User-Id") UUID accountId,
            @PathVariable UUID productId) {
        favoriteService.remove(accountId, productId);
        return ResponseEntity.ok(new ApiResponse<>("Product removed from favorites!", 200, null));
    }
}

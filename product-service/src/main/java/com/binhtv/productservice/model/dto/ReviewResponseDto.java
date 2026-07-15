package com.binhtv.productservice.model.dto;

import java.time.Instant;
import java.util.UUID;

public record ReviewResponseDto(UUID id, UUID productId, UUID accountId, String reviewerName,
        Integer rating, String comment, boolean verifiedPurchase, Instant createdAt, Instant updatedAt) {
}

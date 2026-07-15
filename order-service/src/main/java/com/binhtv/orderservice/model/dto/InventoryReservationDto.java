package com.binhtv.orderservice.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InventoryReservationDto(UUID reservationId, UUID orderId, String status, Instant expiresAt, List<Item> items) {
    public record Item(UUID productId, String productName, String imageUrl, BigDecimal unitPrice,
            BigDecimal discountedUnitPrice, Integer quantity) {}
}

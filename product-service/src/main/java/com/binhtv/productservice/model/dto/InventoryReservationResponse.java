package com.binhtv.productservice.model.dto;

import com.binhtv.productservice.model.entity.ReservationStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record InventoryReservationResponse(UUID reservationId, UUID orderId, ReservationStatus status,
        Instant expiresAt, List<Item> items) {
    public record Item(UUID productId, String productName, String imageUrl, BigDecimal unitPrice,
            BigDecimal discountedUnitPrice, Integer quantity) {}
}

package com.binhtv.orderservice.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderEvent(UUID orderId, String orderNumber, UUID accountId, String email,
        String receiverName, BigDecimal totalAmount, List<Item> items) {
    public record Item(UUID productId, String productName, Integer quantity) {}
}

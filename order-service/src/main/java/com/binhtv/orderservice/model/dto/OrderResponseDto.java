package com.binhtv.orderservice.model.dto;

import com.binhtv.orderservice.model.entity.OrderStatus;
import com.binhtv.orderservice.model.entity.PaymentMethod;
import com.binhtv.orderservice.model.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(UUID id, String orderNumber, OrderStatus status, PaymentMethod paymentMethod,
        PaymentStatus paymentStatus, String receiverName, String email, String phone, String address, String note,
        BigDecimal totalAmount, String paymentUrl, Instant createdAt, List<Item> items) {
    public record Item(UUID productId, String productName, String imageUrl, BigDecimal unitPrice,
            Integer quantity, BigDecimal lineTotal) {}
}

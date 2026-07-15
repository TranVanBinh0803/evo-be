package com.binhtv.productservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product_sales_events", uniqueConstraints =
        @UniqueConstraint(name = "uk_product_sales_order_type", columnNames = {"orderId", "eventType"}))
@Getter @NoArgsConstructor
public class ProductSalesEvent {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;
    private String eventType;
    private Instant processedAt;

    public ProductSalesEvent(UUID orderId, String eventType) {
        this.orderId = orderId;
        this.eventType = eventType;
        this.processedAt = Instant.now();
    }
}

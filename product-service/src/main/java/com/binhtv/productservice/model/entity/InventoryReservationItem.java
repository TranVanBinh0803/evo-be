package com.binhtv.productservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservation_items")
@Getter @Setter @NoArgsConstructor
public class InventoryReservationItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(optional = false) @JoinColumn(name = "reservation_id", nullable = false)
    private InventoryReservation reservation;
    @ManyToOne(optional = false) @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private Integer quantity;
    private String productName;
    private String imageUrl;
    private BigDecimal unitPrice;
    private BigDecimal discountedUnitPrice;
}

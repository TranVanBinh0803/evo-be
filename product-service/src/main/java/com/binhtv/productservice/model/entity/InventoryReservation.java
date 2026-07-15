package com.binhtv.productservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inventory_reservations", uniqueConstraints =
        @UniqueConstraint(name = "uk_inventory_reservation_order", columnNames = "orderId"))
@Getter @Setter @NoArgsConstructor
public class InventoryReservation {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
    private Instant expiresAt;
    private Instant createdAt;
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryReservationItem> items = new ArrayList<>();

    public void addItem(InventoryReservationItem item) {
        item.setReservation(this);
        items.add(item);
    }
}

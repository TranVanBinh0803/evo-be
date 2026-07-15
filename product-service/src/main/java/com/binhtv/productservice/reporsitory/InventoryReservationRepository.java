package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.InventoryReservation;
import com.binhtv.productservice.model.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, UUID> {
    Optional<InventoryReservation> findByOrderId(UUID orderId);
    List<InventoryReservation> findByStatusAndExpiresAtBefore(ReservationStatus status, Instant expiresAt);
}

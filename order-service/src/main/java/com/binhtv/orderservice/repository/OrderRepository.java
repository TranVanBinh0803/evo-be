package com.binhtv.orderservice.repository;

import com.binhtv.orderservice.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPublicId(UUID publicId);
    Optional<Order> findByOrderNumber(String orderNumber);
    Page<Order> findByAccountIdOrderByCreatedAtDesc(UUID accountId, Pageable pageable);
    boolean existsByAccountIdAndStatusNotAndOrderItemsProductId(UUID accountId,
            com.binhtv.orderservice.model.entity.OrderStatus status, UUID productId);
}

package com.binhtv.orderservice.repository;

import com.binhtv.orderservice.model.entity.Order;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}

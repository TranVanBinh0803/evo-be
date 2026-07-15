package com.binhtv.productservice.service;

import com.binhtv.productservice.model.dto.InventoryReservationRequest;
import com.binhtv.productservice.model.dto.InventoryReservationResponse;
import com.binhtv.productservice.model.entity.InventoryReservation;
import com.binhtv.productservice.model.entity.InventoryReservationItem;
import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.model.entity.ReservationStatus;
import com.binhtv.productservice.reporsitory.InventoryReservationRepository;
import com.binhtv.productservice.reporsitory.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryReservationService {
    private final ProductRepository productRepository;
    private final InventoryReservationRepository reservationRepository;

    @Transactional
    public InventoryReservationResponse reserve(InventoryReservationRequest request) {
        return reservationRepository.findByOrderId(request.orderId()).map(this::toResponse).orElseGet(() -> {
            List<UUID> productIds = request.items().stream().map(InventoryReservationRequest.Item::productId).distinct().toList();
            Map<UUID, Product> products = productRepository.findAllByIdForUpdate(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, product -> product));
            if (products.size() != productIds.size()) throw new NoSuchElementException("One or more products were not found.");

            InventoryReservation reservation = new InventoryReservation();
            reservation.setOrderId(request.orderId());
            reservation.setStatus(ReservationStatus.RESERVED);
            reservation.setCreatedAt(Instant.now());
            reservation.setExpiresAt(Instant.now().plusSeconds(900));
            for (InventoryReservationRequest.Item requestedItem : request.items()) {
                Product product = products.get(requestedItem.productId());
                if (product.getQuantity() < requestedItem.quantity()) {
                    throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
                }
                product.setQuantity(product.getQuantity() - requestedItem.quantity());
                InventoryReservationItem item = new InventoryReservationItem();
                item.setProduct(product);
                item.setQuantity(requestedItem.quantity());
                item.setProductName(product.getName());
                item.setImageUrl(product.getImgUrl());
                item.setUnitPrice(product.getPrice());
                item.setDiscountedUnitPrice(discountedPrice(product));
                reservation.addItem(item);
            }
            return toResponse(reservationRepository.save(reservation));
        });
    }

    @Transactional
    public InventoryReservationResponse confirm(UUID id) {
        InventoryReservation reservation = require(id);
        if (reservation.getStatus() == ReservationStatus.RESERVED) reservation.setStatus(ReservationStatus.CONFIRMED);
        return toResponse(reservation);
    }

    @Transactional
    public InventoryReservationResponse release(UUID id) {
        InventoryReservation reservation = require(id);
        releaseStock(reservation, ReservationStatus.RELEASED);
        return toResponse(reservation);
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expireReservations() {
        reservationRepository.findByStatusAndExpiresAtBefore(ReservationStatus.RESERVED, Instant.now())
                .forEach(reservation -> releaseStock(reservation, ReservationStatus.EXPIRED));
    }

    private void releaseStock(InventoryReservation reservation, ReservationStatus targetStatus) {
        if (reservation.getStatus() != ReservationStatus.RESERVED
                && reservation.getStatus() != ReservationStatus.CONFIRMED) return;
        reservation.getItems().forEach(item -> item.getProduct().setQuantity(
                item.getProduct().getQuantity() + item.getQuantity()));
        reservation.setStatus(targetStatus);
    }

    private InventoryReservation require(UUID id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inventory reservation not found."));
    }

    private BigDecimal discountedPrice(Product product) {
        return product.getPrice().multiply(BigDecimal.valueOf(100L - product.getDiscount()).movePointLeft(2))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private InventoryReservationResponse toResponse(InventoryReservation reservation) {
        return new InventoryReservationResponse(reservation.getId(), reservation.getOrderId(), reservation.getStatus(),
                reservation.getExpiresAt(), reservation.getItems().stream().map(item ->
                        new InventoryReservationResponse.Item(item.getProduct().getId(), item.getProductName(),
                                item.getImageUrl(), item.getUnitPrice(), item.getDiscountedUnitPrice(), item.getQuantity())).toList());
    }
}

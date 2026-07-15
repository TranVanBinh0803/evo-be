package com.binhtv.orderservice.service;

import com.binhtv.orderservice.model.dto.InventoryReservationDto;
import com.binhtv.orderservice.model.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryClient {
    private final WebClient.Builder webClientBuilder;
    @Value("${internal.api-key:change-me}") private String internalApiKey;

    public InventoryReservationDto reserve(UUID orderId, List<OrderDto.Item> items) {
        ReservationRequest request = new ReservationRequest(orderId,
                items.stream().map(item -> new ReservationItem(item.productId(), item.quantity())).toList());
        return webClientBuilder.build().post().uri("http://product-service/internal/inventory/reservations")
                .header("X-Internal-Api-Key", internalApiKey).bodyValue(request).retrieve()
                .bodyToMono(InventoryReservationDto.class).block();
    }

    public void confirm(UUID reservationId) { changeState(reservationId, "confirm"); }
    public void release(UUID reservationId) { changeState(reservationId, "release"); }

    private void changeState(UUID reservationId, String action) {
        webClientBuilder.build().post()
                .uri("http://product-service/internal/inventory/reservations/{id}/{action}", reservationId, action)
                .header("X-Internal-Api-Key", internalApiKey).retrieve().toBodilessEntity().block();
    }

    private record ReservationRequest(UUID orderId, List<ReservationItem> items) {}
    private record ReservationItem(UUID productId, Integer quantity) {}
}

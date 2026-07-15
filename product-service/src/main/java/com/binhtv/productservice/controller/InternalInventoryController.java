package com.binhtv.productservice.controller;

import com.binhtv.productservice.model.dto.InventoryReservationRequest;
import com.binhtv.productservice.model.dto.InventoryReservationResponse;
import com.binhtv.productservice.service.InventoryReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@RestController
@RequestMapping("/internal/inventory/reservations")
@RequiredArgsConstructor
public class InternalInventoryController {
    private final InventoryReservationService reservationService;
    @Value("${internal.api-key:change-me}") private String internalApiKey;

    @PostMapping
    public InventoryReservationResponse reserve(@RequestHeader("X-Internal-Api-Key") String apiKey,
            @Valid @RequestBody InventoryReservationRequest request) {
        authorize(apiKey);
        return reservationService.reserve(request);
    }

    @PostMapping("/{id}/confirm")
    public InventoryReservationResponse confirm(@RequestHeader("X-Internal-Api-Key") String apiKey, @PathVariable UUID id) {
        authorize(apiKey);
        return reservationService.confirm(id);
    }

    @PostMapping("/{id}/release")
    public InventoryReservationResponse release(@RequestHeader("X-Internal-Api-Key") String apiKey, @PathVariable UUID id) {
        authorize(apiKey);
        return reservationService.release(id);
    }

    private void authorize(String apiKey) {
        if (!internalApiKey.equals(apiKey)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid internal API key.");
    }
}

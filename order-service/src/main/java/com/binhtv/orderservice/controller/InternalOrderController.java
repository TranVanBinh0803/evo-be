package com.binhtv.orderservice.controller;

import com.binhtv.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
public class InternalOrderController {
    private final OrderService orderService;
    @Value("${internal.api-key:change-me}") private String internalApiKey;

    @GetMapping("/purchases")
    public PurchaseCheckResponse hasPurchased(@RequestHeader("X-Internal-Api-Key") String apiKey,
            @RequestParam UUID accountId, @RequestParam UUID productId) {
        if (!internalApiKey.equals(apiKey)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid internal API key.");
        return new PurchaseCheckResponse(orderService.hasPurchased(accountId, productId));
    }

    public record PurchaseCheckResponse(boolean purchased) {}
}

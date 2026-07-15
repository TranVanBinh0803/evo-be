package com.binhtv.productservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderPurchaseClient {
    private final WebClient.Builder webClientBuilder;

    @Value("${internal.api-key:change-me}")
    private String internalApiKey;

    public boolean hasPurchased(UUID accountId, UUID productId) {
        PurchaseCheckResponse response = webClientBuilder.build().get()
                .uri("http://order-service/internal/orders/purchases?accountId={accountId}&productId={productId}",
                        accountId, productId)
                .header("X-Internal-Api-Key", internalApiKey)
                .retrieve()
                .bodyToMono(PurchaseCheckResponse.class)
                .block();
        return response != null && response.purchased();
    }

    private record PurchaseCheckResponse(boolean purchased) {
    }
}

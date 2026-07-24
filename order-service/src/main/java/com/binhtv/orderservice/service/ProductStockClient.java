package com.binhtv.orderservice.service;

import com.binhtv.orderservice.model.dto.ProductStockApiResponse;
import com.binhtv.orderservice.model.dto.ProductStockRequestDto;
import com.binhtv.orderservice.model.dto.ProductStockResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductStockClient {
    private final WebClient.Builder loadBalancedWebClientBuilder;

    @CircuitBreaker(name = "product", fallbackMethod = "checkStockFallback")
    public ProductStockResponseDto checkStock(ProductStockRequestDto request) {
        ProductStockApiResponse response = loadBalancedWebClientBuilder.build()
                .post()
                .uri("http://product-service/api/products/stock/check")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductStockApiResponse.class)
                .block();

        if (response == null || response.getData() == null) {
            throw new IllegalStateException("Product service returned an empty stock response.");
        }
        return response.getData();
    }

    private ProductStockResponseDto checkStockFallback(ProductStockRequestDto request, RuntimeException exception) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Product service is unavailable. Please try again later.",
                exception);
    }
}

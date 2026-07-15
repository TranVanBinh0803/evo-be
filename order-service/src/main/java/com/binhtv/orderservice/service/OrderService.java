package com.binhtv.orderservice.service;

import com.binhtv.orderservice.model.dto.OrderDto;
import com.binhtv.orderservice.model.dto.OrderResponseDto;
import com.binhtv.orderservice.model.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface OrderService {
    OrderResponseDto createOrder(UUID accountId, OrderDto request, String clientIp);
    PageResponse<OrderResponseDto> getOrders(UUID accountId, Pageable pageable);
    OrderResponseDto getOrder(UUID accountId, UUID orderId);
    OrderResponseDto cancel(UUID accountId, UUID orderId);
    boolean hasPurchased(UUID accountId, UUID productId);
    VnpayResult handleVnpayIpn(Map<String, String> params);
    record VnpayResult(String responseCode, String message) {}
}

package com.binhtv.orderservice.controller;

import com.binhtv.orderservice.model.dto.ApiResponse;
import com.binhtv.orderservice.model.dto.OrderDto;
import com.binhtv.orderservice.model.dto.OrderResponseDto;
import com.binhtv.orderservice.model.dto.PageResponse;
import com.binhtv.orderservice.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(
            @RequestHeader("X-Authenticated-User-Id") UUID accountId,
            @Valid @RequestBody OrderDto request, HttpServletRequest servletRequest) {
        OrderResponseDto order = orderService.createOrder(accountId, request, servletRequest.getRemoteAddr());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Order created successfully!", HttpStatus.CREATED.value(), order));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponseDto>>> getOrders(
            @RequestHeader("X-Authenticated-User-Id") UUID accountId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(new ApiResponse<>("Get orders successful!", HttpStatus.OK.value(),
                orderService.getOrders(accountId, PageRequest.of(page, size))));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(
            @RequestHeader("X-Authenticated-User-Id") UUID accountId, @PathVariable UUID orderId) {
        return ResponseEntity.ok(new ApiResponse<>("Get order successful!", HttpStatus.OK.value(),
                orderService.getOrder(accountId, orderId)));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancel(
            @RequestHeader("X-Authenticated-User-Id") UUID accountId, @PathVariable UUID orderId) {
        return ResponseEntity.ok(new ApiResponse<>("Order cancelled successfully!", HttpStatus.OK.value(),
                orderService.cancel(accountId, orderId)));
    }
}

package com.binhtv.orderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.binhtv.orderservice.event.Producer;
import com.binhtv.orderservice.model.dto.OrderDto;
import com.binhtv.orderservice.model.dto.OrderItemDto;
import com.binhtv.orderservice.model.dto.ProductStockItemRequest;
import com.binhtv.orderservice.model.dto.ProductStockRequestDto;
import com.binhtv.orderservice.model.dto.ProductStockResponse;
import com.binhtv.orderservice.model.dto.ProductStockResponseDto;
import com.binhtv.orderservice.model.entity.Order;
import com.binhtv.orderservice.model.entity.OrderItem;
import com.binhtv.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService{
    private final Producer producer;
    private final ProductStockClient productStockClient;
    private final OrderRepository orderRepository;

    public void createOrder(OrderDto orderDto) {
        validateOrderItems(orderDto);
        ProductStockRequestDto stockRequest = new ProductStockRequestDto(
                orderDto.getOrderItemDtoList().stream()
                        .map(item -> new ProductStockItemRequest(item.getRef(), item.getQuantity()))
                        .toList());

        ProductStockResponseDto stockResponse = productStockClient.checkStock(stockRequest);
        if (!stockResponse.isAllInStock()) {
            String unavailableProducts = stockResponse.getProductStockResponses().stream()
                    .filter(response -> !response.isInStock())
                    .map(this::formatUnavailableProduct)
                    .collect(Collectors.joining(", "));
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Insufficient product stock: " + unavailableProducts);
        }

        orderRepository.save(mapToEntity(orderDto));
        // String userPhoneNumber = String.valueOf(new Random().nextInt(900000000) + 1000000000);
        // producer.sendEvent(userPhoneNumber);
    }

    private void validateOrderItems(OrderDto orderDto) {
        if (orderDto == null || orderDto.getOrderItemDtoList() == null || orderDto.getOrderItemDtoList().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order must contain at least one product.");
        }
        boolean invalidItem = orderDto.getOrderItemDtoList().stream()
                .anyMatch(item -> item == null || item.getRef() == null
                        || item.getQuantity() == null || item.getQuantity() < 1);
        if (invalidItem) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Each order item must contain a product ID and quantity of at least 1.");
        }
    }

    private String formatUnavailableProduct(ProductStockResponse response) {
        return "%s (requested=%d, available=%d)".formatted(
                response.getProductId(),
                response.getRequestedQuantity(),
                response.getAvailableQuantity());
    }

    private Order mapToEntity(OrderDto orderDto){
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderDto.getOrderItemDtoList()){
            OrderItem orderItem = OrderItem.builder()
                    .ref(orderItemDto.getRef())
                    .price(orderItemDto.getPrice())
                    .quantity(orderItemDto.getQuantity())
                    .build();
            orderItems.add(orderItem);
        }

        return Order.builder()
                .orderItems(orderItems)
                .build();
    }
}

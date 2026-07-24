package com.binhtv.orderservice.controller;

import com.binhtv.orderservice.model.dto.OrderDto;
import com.binhtv.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDto orderDto) {
        orderService.createOrder(orderDto);
        return new ResponseEntity<>("Order created succesfully.", HttpStatus.CREATED);
    }
}

package com.binhtv.orderservice.service;

import com.binhtv.orderservice.model.dto.OrderDto;

public interface OrderService{
    void createOrder(OrderDto orderDto);
}

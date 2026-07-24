package com.binhtv.orderservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockApiResponse {
    private String message;
    private int statusCode;
    private ProductStockResponseDto data;
}

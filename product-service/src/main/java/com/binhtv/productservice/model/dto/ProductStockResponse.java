package com.binhtv.productservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockResponse {
    private UUID productId;
    private int requestedQuantity;
    private int availableQuantity;
    private boolean inStock;
}

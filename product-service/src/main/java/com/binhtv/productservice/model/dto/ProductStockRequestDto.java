package com.binhtv.productservice.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockRequestDto {
    @NotEmpty(message = "Product items are required.")
    private List<@Valid ProductStockItemRequest> items;
}

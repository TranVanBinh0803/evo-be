package com.binhtv.productservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String imgUrl;
    private Integer discount;
    private Integer quantity;
    private String description;
    private UUID brandId;
    private String brandName;
    private UUID categoryId;
    private String categoryName;
    private String categoryImgUrl;
}
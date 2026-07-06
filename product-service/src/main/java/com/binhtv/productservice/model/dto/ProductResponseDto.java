package com.binhtv.productservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
   private String name;
    private BigDecimal price;
    private Integer discount;
    private Integer quantity;
    private String description;
    private Brand brand_id;
    private Category category_id;
}

package com.binhtv.productservice.model.dto;

import lombok.*;

import java.math.BigDecimal;

import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private BigDecimal price;
    private Integer discount;
    private Integer quantity;
    private String description;
    private Brand brand_id;
    private Category category_id;
}

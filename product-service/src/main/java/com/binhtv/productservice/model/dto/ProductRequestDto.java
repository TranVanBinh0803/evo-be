package com.binhtv.productservice.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class ProductRequestDto {
    @NotBlank(message = "Product name is required.")
    @Size(min = 2, max = 150, message = "Product name must be between 2 and 150 characters.")
    private String name;

    @NotNull(message = "Product price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0.")
    private BigDecimal price;

    @NotBlank(message = "Image url is required.")
    private String imgUrl;

    @NotNull(message = "Product discount is required.")
    @Min(value = 0, message = "Product discount must be at least 0.")
    @Max(value = 100, message = "Product discount must not exceed 100.")
    private Integer discount;

    @NotNull(message = "Product quantity is required.")
    @Min(value = 0, message = "Product quantity must be at least 0.")
    private Integer quantity;

    @Size(max = 1000, message = "Product description must not exceed 1000 characters.")
    private String description;

    @Size(max = 100, message = "Product origin must not exceed 100 characters.")
    private String origin;

    @NotNull(message = "Product brand id is required.")
    private UUID brandId;

    @NotNull(message = "Product category id is required.")
    private UUID categoryId;
}

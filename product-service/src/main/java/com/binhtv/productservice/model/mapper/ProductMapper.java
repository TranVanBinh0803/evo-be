package com.binhtv.productservice.model.mapper;

import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toEntity(ProductRequestDto request, Brand brand, Category category) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .imgUrl(request.getImgUrl())
                .discount(request.getDiscount())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .origin(request.getOrigin())
                .brand(brand)
                .category(category)
                .build();
    }

    public ProductResponseDto toDto(Product product) {
        Brand brand = product.getBrand();
        Category category = product.getCategory();

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .imgUrl(product.getImgUrl())
                .discount(product.getDiscount())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .origin(product.getOrigin())
                .averageRating(product.getAverageRating())
                .reviewCount(product.getReviewCount())
                .soldCount(product.getSoldCount())
                .createdAt(product.getCreatedAt())
                .brandId(brand == null ? null : brand.getId())
                .brandName(brand == null ? null : brand.getName())
                .categoryId(category == null ? null : category.getId())
                .categoryName(category == null ? null : category.getName())
                .categoryImgUrl(category == null ? null : category.getImgUrl())
                .build();
    }
}

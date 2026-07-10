package com.binhtv.productservice.service;

import com.binhtv.productservice.event.Producer;
import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.entity.Brand;
import com.binhtv.productservice.model.entity.Category;
import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.reporsitory.BrandRepository;
import com.binhtv.productservice.reporsitory.CategoryRepository;
import com.binhtv.productservice.reporsitory.ProductRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final Producer producer;

    public ProductResponseDto getById(UUID id) {
        return productRepository.findByIdWithBrandAndCategory(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new NoSuchElementException("Product not found."));
    }

    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Brand brand = brandRepository.findById(productRequestDto.getBrandId())
                .orElseThrow(() -> new NoSuchElementException("Brand not found."));
        Category category = categoryRepository.findById(productRequestDto.getCategoryId())
                .orElseThrow(() -> new NoSuchElementException("Category not found."));

        Product product = Product.builder()
                .name(productRequestDto.getName())
                .price(productRequestDto.getPrice())
                .imgUrl(productRequestDto.getImgUrl())
                .discount(productRequestDto.getDiscount())
                .quantity(productRequestDto.getQuantity())
                .description(productRequestDto.getDescription())
                .brand(brand)
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
        // Sent event
        // try {
        //     producer.sendMessage(productEventModel);
        // } catch (JacksonException ex) {
        //     log.info("Message could not sent to queue");
        // }
    }

    public List<ProductResponseDto> getProducts() {
        return productRepository.findAllWithBrandAndCategory().stream()
                .map(this::mapToDto)
                .toList();
    }

    private ProductResponseDto mapToDto(Product product) {
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
                .brandId(brand == null ? null : brand.getId())
                .brandName(brand == null ? null : brand.getName())
                .categoryId(category == null ? null : category.getId())
                .categoryName(category == null ? null : category.getName())
                .categoryImgUrl(category == null ? null : category.getImgUrl())
                .build();
    }
}
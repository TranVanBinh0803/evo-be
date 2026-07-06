package com.binhtv.productservice.service;

import com.binhtv.productservice.event.Producer;
import com.binhtv.productservice.model.dto.ProductEventModel;
import com.binhtv.productservice.model.dto.ProductRequestDto;
import com.binhtv.productservice.model.dto.ProductResponseDto;
import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.reporsitory.ProductRepository;
import tools.jackson.core.JacksonException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Producer producer;

    @Override
    public ProductResponseDto getById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            return null;
        }
        return mapToDto(product.get());
    }

    @Override
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(productRequestDto.getName())
                .price(productRequestDto.getPrice())
                .discount(productRequestDto.getDiscount())
                .quantity(productRequestDto.getQuantity())
                .description(productRequestDto.getDescription())
                .brand_id(productRequestDto.getBrand_id())
                .category_id(productRequestDto.getCategory_id())
                .build();

        Product savedProduct = productRepository.save(product);

        // Sent event

        return mapToDto(savedProduct);

        // try {
        //     producer.sendMessage(productEventModel);
        // } catch (JacksonException ex) {
        //     log.info("Message could not sent to queue");
        // }
    }

    @Override
    public List<ProductResponseDto> getProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ProductResponseDto mapToDto(Product product) {
        return ProductResponseDto.builder()
                .name(product.getName())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .brand_id(product.getBrand_id())
                .category_id(product.getCategory_id())
                .build();
    }
}

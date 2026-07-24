package com.binhtv.productstockservice.service;

import com.binhtv.productstockservice.model.dto.CreateProductDto;
import com.binhtv.productstockservice.model.dto.ProductStockResponse;
import com.binhtv.productstockservice.model.dto.ProductStockResponseDto;
import com.binhtv.productstockservice.model.entity.Product;
import com.binhtv.productstockservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void create(CreateProductDto createProductDto) {
        productRepository.save(mapToEntity(createProductDto));
    }

    public ProductStockResponseDto checkProductsStock(List<UUID> productRefs) {
        Optional<List<Product>> products = productRepository.findByRefIn(productRefs);
        if (products.isPresent() && products.get().size() > 0) {
            List<ProductStockResponse> productStockResponses = products.get().stream()
                    .map(product -> {
                        return ProductStockResponse.builder()
                                .ref(product.getRef())
                                .inStock(product.getStockCount() > 0 ? product.getInStock() : false)
                                .build();
                    }).collect(Collectors.toList());
            return ProductStockResponseDto.builder().productStockResponses(productStockResponses).build();
        }
        ProductStockResponseDto productStockResponseDto = new ProductStockResponseDto();
        productStockResponseDto.setProductStockResponses(null);
        productStockResponseDto.setError(true);
        productStockResponseDto.setDescription("Product refs does not contain in database");
        return productStockResponseDto;
    }

    private Product mapToEntity(CreateProductDto createProductDto) {
        return Product.builder()
                .ref(createProductDto.getRef())
                .price(createProductDto.getPrice())
                .inStock(createProductDto.getInStock())
                .name(createProductDto.getName())
                .stockCount(createProductDto.getStockCount())
                .build();
    }
}

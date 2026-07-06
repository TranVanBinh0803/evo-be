package com.binhtv.productstockservice.service;

import com.binhtv.productstockservice.model.dto.CreateProductDto;
import com.binhtv.productstockservice.model.dto.ProductStockResponse;
import com.binhtv.productstockservice.model.dto.ProductStockResponseDto;

import java.util.List;

public interface ProductService {
    //ProductResponseDto getById(Long id);
    void create(CreateProductDto createProductDto);
    ProductStockResponseDto checkProductsStock(List<String> productRefs);
    //List<ProductResponseDto> getProducts();
}

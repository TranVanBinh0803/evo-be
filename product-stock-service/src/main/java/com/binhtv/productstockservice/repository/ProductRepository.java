package com.binhtv.productstockservice.repository;

import com.binhtv.productstockservice.model.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends MongoRepository<Product, String > {
    Optional<List<Product>> findByRefIn(List<UUID> productRefs);
}

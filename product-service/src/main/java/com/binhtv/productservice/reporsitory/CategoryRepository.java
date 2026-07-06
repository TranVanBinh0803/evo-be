package com.binhtv.productservice.reporsitory;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binhtv.productservice.model.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID>{

}

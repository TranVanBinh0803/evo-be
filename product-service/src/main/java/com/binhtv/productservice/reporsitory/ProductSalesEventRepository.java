package com.binhtv.productservice.reporsitory;

import com.binhtv.productservice.model.entity.ProductSalesEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductSalesEventRepository extends JpaRepository<ProductSalesEvent, UUID> {
    boolean existsByOrderIdAndEventType(UUID orderId, String eventType);
}

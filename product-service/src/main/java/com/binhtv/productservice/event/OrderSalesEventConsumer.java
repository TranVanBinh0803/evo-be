package com.binhtv.productservice.event;

import com.binhtv.productservice.model.entity.Product;
import com.binhtv.productservice.model.entity.ProductSalesEvent;
import com.binhtv.productservice.reporsitory.ProductRepository;
import com.binhtv.productservice.reporsitory.ProductSalesEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderSalesEventConsumer {
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final ProductSalesEventRepository eventRepository;

    @KafkaListener(topics = "order.placed", groupId = "product-sales")
    public void placed(String payload) throws JacksonException { apply(payload, "PLACED", 1); }

    @KafkaListener(topics = "order.cancelled", groupId = "product-sales")
    public void cancelled(String payload) throws JacksonException { apply(payload, "CANCELLED", -1); }

    @Transactional
    void apply(String payload, String type, int direction) throws JacksonException {
        OrderEvent event = objectMapper.readValue(payload, OrderEvent.class);
        if (eventRepository.existsByOrderIdAndEventType(event.orderId(), type)) return;
        List<UUID> ids = event.items().stream().map(Item::productId).distinct().toList();
        Map<UUID, Product> products = productRepository.findAllByIdForUpdate(ids).stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        event.items().forEach(item -> {
            Product product = products.get(item.productId());
            if (product != null) product.setSoldCount(Math.max(0, product.getSoldCount() + (long) direction * item.quantity()));
        });
        eventRepository.save(new ProductSalesEvent(event.orderId(), type));
    }

    private record OrderEvent(UUID orderId, List<Item> items) {}
    private record Item(UUID productId, Integer quantity) {}
}

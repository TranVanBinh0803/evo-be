package com.binhtv.orderservice.event;

import com.binhtv.orderservice.model.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void placed(Order order) { publish("order.placed", order); }
    public void cancelled(Order order) { publish("order.cancelled", order); }

    private void publish(String topic, Order order) {
        try {
            OrderEvent event = new OrderEvent(order.getPublicId(), order.getOrderNumber(), order.getAccountId(),
                    order.getEmail(), order.getReceiverName(), order.getTotalAmount(), order.getOrderItems().stream()
                    .map(item -> new OrderEvent.Item(item.getProductId(), item.getProductName(), item.getQuantity())).toList());
            kafkaTemplate.send(topic, order.getPublicId().toString(), objectMapper.writeValueAsString(event));
        } catch (JacksonException exception) {
            throw new IllegalStateException("Could not serialize order event.", exception);
        }
    }
}

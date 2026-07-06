package com.binhtv.productservice.event;

import com.binhtv.productservice.model.dto.ProductEventModel;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Producer {
    private final String CREATE_PRODUCT_TOPIC = "create.product";
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(ProductEventModel productEventModel) throws JacksonException {
        String productDtoAsString = objectMapper.writeValueAsString(productEventModel);
        kafkaTemplate.send(CREATE_PRODUCT_TOPIC, productDtoAsString);
        log.info("Sent product {}", productDtoAsString);
    }
}

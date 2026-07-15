package com.binhtv.orderservice.service;

import com.binhtv.orderservice.model.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class VnpayServiceTest {
    private VnpayService service;

    @BeforeEach
    void setUp() {
        service = new VnpayService();
        ReflectionTestUtils.setField(service, "paymentUrl", "https://sandbox.example/pay");
        ReflectionTestUtils.setField(service, "tmnCode", "EVO123");
        ReflectionTestUtils.setField(service, "hashSecret", "test-hash-secret");
        ReflectionTestUtils.setField(service, "returnUrl", "http://localhost:5173/thanh-toan/ket-qua");
    }

    @Test
    void generatedPaymentUrlHasAValidSignatureAndAmount() {
        Order order = order();

        Map<String, String> params = queryParams(service.createPaymentUrl(order, "127.0.0.1"));

        assertThat(params.get("vnp_Amount")).isEqualTo("12500000");
        assertThat(params.get("vnp_TxnRef")).isEqualTo("EVO-1001");
        assertThat(service.verify(params)).isTrue();
    }

    @Test
    void changedPaymentAmountInvalidatesSignature() {
        Map<String, String> params = queryParams(service.createPaymentUrl(order(), "127.0.0.1"));
        params.put("vnp_Amount", "1");

        assertThat(service.verify(params)).isFalse();
    }

    private Order order() {
        Order order = new Order();
        order.setPublicId(UUID.fromString("aaaaaaaa-aaaa-4aaa-8aaa-aaaaaaaaaaaa"));
        order.setOrderNumber("EVO-1001");
        order.setTotalAmount(new BigDecimal("125000"));
        order.setCreatedAt(Instant.parse("2026-07-15T00:00:00Z"));
        return order;
    }

    private Map<String, String> queryParams(String url) {
        return Arrays.stream(URI.create(url).getRawQuery().split("&"))
                .map(value -> value.split("=", 2))
                .collect(Collectors.toMap(
                        pair -> decode(pair[0]),
                        pair -> pair.length == 2 ? decode(pair[1]) : ""));
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}

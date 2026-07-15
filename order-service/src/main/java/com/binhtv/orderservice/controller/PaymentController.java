package com.binhtv.orderservice.controller;

import com.binhtv.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payments/vnpay")
@RequiredArgsConstructor
public class PaymentController {
    private final OrderService orderService;

    @GetMapping("/ipn")
    public VnpayIpnResponse ipn(@RequestParam Map<String, String> params) {
        OrderService.VnpayResult result = orderService.handleVnpayIpn(params);
        return new VnpayIpnResponse(result.responseCode(), result.message());
    }

    public record VnpayIpnResponse(String RspCode, String Message) {}
}

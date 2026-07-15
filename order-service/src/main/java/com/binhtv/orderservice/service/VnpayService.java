package com.binhtv.orderservice.service;

import com.binhtv.orderservice.model.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VnpayService {
    @Value("${vnpay.payment-url:https://sandbox.vnpayment.vn/paymentv2/vpcpay.html}") private String paymentUrl;
    @Value("${vnpay.tmn-code:replace-me}") private String tmnCode;
    @Value("${vnpay.hash-secret:replace-me}") private String hashSecret;
    @Value("${vnpay.return-url:http://localhost:5173/thanh-toan/ket-qua}") private String returnUrl;

    public String createPaymentUrl(Order order, String clientIp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        Map<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", order.getTotalAmount().movePointRight(2).toBigInteger().toString());
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", order.getOrderNumber());
        params.put("vnp_OrderInfo", "Thanh toan don hang " + order.getOrderNumber());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl + "?orderId=" + order.getPublicId());
        params.put("vnp_IpAddr", clientIp == null ? "127.0.0.1" : clientIp);
        params.put("vnp_CreateDate", formatter.format(order.getCreatedAt()));
        params.put("vnp_ExpireDate", formatter.format(order.getCreatedAt().plusSeconds(900)));
        String query = canonicalQuery(params);
        return paymentUrl + "?" + query + "&vnp_SecureHash=" + hmacSha512(query);
    }

    public boolean verify(Map<String, String> input) {
        String signature = input.get("vnp_SecureHash");
        if (signature == null) return false;
        Map<String, String> signed = new TreeMap<>(input);
        signed.remove("vnp_SecureHash");
        signed.remove("vnp_SecureHashType");
        return constantTimeEquals(signature, hmacSha512(canonicalQuery(signed)));
    }

    private String canonicalQuery(Map<String, String> params) {
        return params.entrySet().stream().filter(entry -> entry.getValue() != null && !entry.getValue().isBlank())
                .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue())).collect(Collectors.joining("&"));
    }

    private String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8); }

    private String hmacSha512(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(hashSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));
            byte[] bytes = mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(bytes.length * 2);
            for (byte item : bytes) result.append(String.format("%02x", item));
            return result.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Could not sign VNPay request.", exception);
        }
    }

    private boolean constantTimeEquals(String left, String right) {
        return java.security.MessageDigest.isEqual(left.toLowerCase().getBytes(StandardCharsets.UTF_8),
                right.toLowerCase().getBytes(StandardCharsets.UTF_8));
    }
}

package com.binhtv.orderservice.service;

import com.binhtv.orderservice.event.OrderEventProducer;
import com.binhtv.orderservice.model.dto.InventoryReservationDto;
import com.binhtv.orderservice.model.dto.OrderDto;
import com.binhtv.orderservice.model.dto.OrderResponseDto;
import com.binhtv.orderservice.model.dto.PageResponse;
import com.binhtv.orderservice.model.entity.Order;
import com.binhtv.orderservice.model.entity.OrderItem;
import com.binhtv.orderservice.model.entity.OrderStatus;
import com.binhtv.orderservice.model.entity.PaymentMethod;
import com.binhtv.orderservice.model.entity.PaymentStatus;
import com.binhtv.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final VnpayService vnpayService;
    private final OrderEventProducer eventProducer;

    @Override
    @Transactional
    public OrderResponseDto createOrder(UUID accountId, OrderDto request, String clientIp) {
        Order order = new Order();
        order.setPublicId(UUID.randomUUID());
        order.setOrderNumber("EVO-" + order.getPublicId().toString().substring(0, 8).toUpperCase());
        order.setAccountId(accountId);
        order.setReceiverName(request.receiverName());
        order.setEmail(request.email());
        order.setPhone(request.phone());
        order.setAddress(request.address());
        order.setNote(request.note());
        order.setPaymentMethod(request.paymentMethod());
        order.setCreatedAt(Instant.now());

        InventoryReservationDto reservation = inventoryClient.reserve(order.getPublicId(), request.items());
        if (reservation == null) throw new IllegalStateException("Product service did not return an inventory reservation.");
        order.setReservationId(reservation.reservationId());
        BigDecimal total = BigDecimal.ZERO;
        for (InventoryReservationDto.Item reservedItem : reservation.items()) {
            OrderItem item = new OrderItem();
            item.setProductId(reservedItem.productId());
            item.setProductName(reservedItem.productName());
            item.setImageUrl(reservedItem.imageUrl());
            item.setUnitPrice(reservedItem.discountedUnitPrice());
            item.setQuantity(reservedItem.quantity());
            item.setLineTotal(reservedItem.discountedUnitPrice().multiply(BigDecimal.valueOf(reservedItem.quantity())));
            total = total.add(item.getLineTotal());
            order.addItem(item);
        }
        order.setTotalAmount(total);

        String paymentUrl = null;
        try {
            if (request.paymentMethod() == PaymentMethod.COD) {
                order.setStatus(OrderStatus.PENDING);
                order.setPaymentStatus(PaymentStatus.UNPAID);
                inventoryClient.confirm(reservation.reservationId());
            } else {
                order.setStatus(OrderStatus.PENDING_PAYMENT);
                order.setPaymentStatus(PaymentStatus.PENDING);
            }
            orderRepository.saveAndFlush(order);
            if (request.paymentMethod() == PaymentMethod.VNPAY) paymentUrl = vnpayService.createPaymentUrl(order, clientIp);
            if (request.paymentMethod() == PaymentMethod.COD) eventProducer.placed(order);
            return toResponse(order, paymentUrl);
        } catch (RuntimeException exception) {
            inventoryClient.release(reservation.reservationId());
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<OrderResponseDto> getOrders(UUID accountId, Pageable pageable) {
        return PageResponse.from(orderRepository.findByAccountIdOrderByCreatedAtDesc(accountId, pageable)
                .map(order -> toResponse(order, null)));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(UUID accountId, UUID orderId) {
        return toResponse(requireOwnOrder(accountId, orderId), null);
    }

    @Override
    @Transactional
    public OrderResponseDto cancel(UUID accountId, UUID orderId) {
        Order order = requireOwnOrder(accountId, orderId);
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalArgumentException("This order can no longer be cancelled online.");
        }
        inventoryClient.release(order.getReservationId());
        order.setStatus(OrderStatus.CANCELLED);
        if (order.getPaymentStatus() == PaymentStatus.PENDING) order.setPaymentStatus(PaymentStatus.FAILED);
        eventProducer.cancelled(order);
        return toResponse(order, null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPurchased(UUID accountId, UUID productId) {
        return orderRepository.existsByAccountIdAndStatusNotAndOrderItemsProductId(accountId, OrderStatus.CANCELLED, productId);
    }

    @Override
    @Transactional
    public VnpayResult handleVnpayIpn(Map<String, String> params) {
        if (!vnpayService.verify(params)) return new VnpayResult("97", "Invalid signature");
        Order order = orderRepository.findByOrderNumber(params.get("vnp_TxnRef")).orElse(null);
        if (order == null) return new VnpayResult("01", "Order not found");
        String expectedAmount = order.getTotalAmount().movePointRight(2).toBigInteger().toString();
        if (!expectedAmount.equals(params.get("vnp_Amount"))) return new VnpayResult("04", "Invalid amount");
        if (order.getPaymentStatus() != PaymentStatus.PENDING) return new VnpayResult("02", "Order already processed");

        if ("00".equals(params.get("vnp_ResponseCode")) && "00".equals(params.get("vnp_TransactionStatus"))) {
            inventoryClient.confirm(order.getReservationId());
            order.setStatus(OrderStatus.CONFIRMED);
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setPaymentTransactionRef(params.get("vnp_TransactionNo"));
            eventProducer.placed(order);
        } else {
            inventoryClient.release(order.getReservationId());
            order.setStatus(OrderStatus.CANCELLED);
            order.setPaymentStatus(PaymentStatus.FAILED);
        }
        return new VnpayResult("00", "Confirm success");
    }

    private Order requireOwnOrder(UUID accountId, UUID orderId) {
        Order order = orderRepository.findByPublicId(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found."));
        if (!order.getAccountId().equals(accountId)) throw new NoSuchElementException("Order not found.");
        return order;
    }

    private OrderResponseDto toResponse(Order order, String paymentUrl) {
        return new OrderResponseDto(order.getPublicId(), order.getOrderNumber(), order.getStatus(),
                order.getPaymentMethod(), order.getPaymentStatus(), order.getReceiverName(), order.getEmail(),
                order.getPhone(), order.getAddress(), order.getNote(), order.getTotalAmount(), paymentUrl,
                order.getCreatedAt(), order.getOrderItems().stream().map(item -> new OrderResponseDto.Item(
                        item.getProductId(), item.getProductName(), item.getImageUrl(), item.getUnitPrice(),
                        item.getQuantity(), item.getLineTotal())).toList());
    }
}

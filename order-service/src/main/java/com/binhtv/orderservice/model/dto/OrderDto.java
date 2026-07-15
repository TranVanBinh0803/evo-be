package com.binhtv.orderservice.model.dto;

import com.binhtv.orderservice.model.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record OrderDto(
        @NotEmpty List<@Valid Item> items,
        @NotBlank String receiverName,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String address,
        @Size(max = 500) String note,
        @NotNull PaymentMethod paymentMethod) {
    public record Item(@NotNull UUID productId, @NotNull @Min(1) Integer quantity) {}
}

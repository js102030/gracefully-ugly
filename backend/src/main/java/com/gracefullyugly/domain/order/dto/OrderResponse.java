package com.gracefullyugly.domain.order.dto;

import com.gracefullyugly.domain.orderitem.dto.ItemInfoToPaymentDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    @NotNull
    private Long orderId;
    @NotNull
    private Long userId;
    @NotNull
    private String address;
    @NotNull
    private String phoneNumber;
    @Valid
    @NotNull
    private ItemInfoToPaymentDto ItemInfoToPayment;
}

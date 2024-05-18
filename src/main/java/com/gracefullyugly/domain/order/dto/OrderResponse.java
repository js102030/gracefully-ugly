package com.gracefullyugly.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private Long userId;

    private String address;

    private String phoneNumber;

    private int totalPrice;

}

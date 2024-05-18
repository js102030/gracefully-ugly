package com.gracefullyugly.domain.order.dto;

import com.gracefullyugly.domain.order.entity.Order;

public class OrderDtoUtil {

    public static OrderResponse ordertoOrderResponse(Order order, int totalPrice) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .address(order.getAddress())
                .phoneNumber(order.getPhoneNumber())
                .totalPrice(totalPrice)
                .build();
    }

}
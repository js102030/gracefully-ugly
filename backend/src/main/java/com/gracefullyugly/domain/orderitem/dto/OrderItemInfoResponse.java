package com.gracefullyugly.domain.orderitem.dto;

public interface OrderItemInfoResponse {

    Long getItemId();
    String getName();
    Long getPrice();
    Long getQuantity();
}

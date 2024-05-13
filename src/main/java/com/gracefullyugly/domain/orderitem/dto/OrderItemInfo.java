package com.gracefullyugly.domain.orderitem.dto;

public interface OrderItemInfo {

    String getItemName();

    String getProductionPlace();

    Integer getOrderItemQuantity();

    Integer getItemPrice();

    String getAddress();

    String getOrderCreatedDate();
}

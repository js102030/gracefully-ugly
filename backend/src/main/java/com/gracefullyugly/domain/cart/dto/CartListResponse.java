package com.gracefullyugly.domain.cart.dto;


public interface CartListResponse {

    Long getCartItemId();
    Long getItemCount();
    Long getItemId();
    String getName();
    int getPrice();
    Long getCategoryId();
}

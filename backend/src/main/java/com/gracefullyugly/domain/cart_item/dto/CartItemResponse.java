package com.gracefullyugly.domain.cart_item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private String message;
    private Long itemCount;


}

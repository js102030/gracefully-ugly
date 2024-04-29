package com.gracefullyugly.domain.cart.dto;

import com.gracefullyugly.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartListResponse {
    private Item cartListItem;
    private Long itemCount;
}

package com.gracefullyugly.domain.cart.dto;

import com.gracefullyugly.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartListResponse {
    private Item cartListItem;
    private Long itemCount;
}

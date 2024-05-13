package com.gracefullyugly.domain.orderitem.dto;

import com.gracefullyugly.domain.item.enumtype.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemInfoResponse {

    private Long itemId;
    private Category category;
    private String itemName;
    private int itemPrice;
    private int quantity;
}

package com.gracefullyugly.domain.orderitem.dto;

import com.gracefullyugly.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ItemOrderDetails {

    private Item item;
    private int quantity;

}

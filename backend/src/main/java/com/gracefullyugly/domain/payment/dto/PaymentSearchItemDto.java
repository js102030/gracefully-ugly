package com.gracefullyugly.domain.payment.dto;

import com.gracefullyugly.domain.item.enumtype.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSearchItemDto {

    private Long itemId;
    private Category category;
    private String itemName;
    private int itemPrice;
    private int quantity;
}

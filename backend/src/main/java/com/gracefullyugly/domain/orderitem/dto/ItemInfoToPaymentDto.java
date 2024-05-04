package com.gracefullyugly.domain.orderitem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInfoToPaymentDto {

    private String itemName;
    private int quantity = 0;
    private int totalAmount = 0;
    private int taxFreeAmount = 0;

    public ItemInfoToPaymentDto setFirstItemName(String itemName) {
        if (quantity == 0) {
            this.itemName = itemName;
        }

        return this;
    }
    public ItemInfoToPaymentDto addQuantity(int quantity) {
        this.quantity += quantity;
        return this;
    }

    public ItemInfoToPaymentDto addTotalAmount(int totalAmount) {
        this.totalAmount += totalAmount;
        return this;
    }
}

package com.gracefullyugly.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSearchDTO {

    private PaymentSearchOrderDto order;

    private PaymentSearchItemDto item;

    private PaymentSearchPaymentDto payment;
}

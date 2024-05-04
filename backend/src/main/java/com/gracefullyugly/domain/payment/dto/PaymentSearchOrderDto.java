package com.gracefullyugly.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSearchOrderDto {

    private Long orderId;
    private String address;
    private String phoneNumber;
}

package com.gracefullyugly.domain.payment.dto;

import java.time.LocalDateTime;
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

    private PaymentSearchPaymentDto payment;

    public PaymentSearchDTO(Long orderId, String address, String phoneNumber, Long paymentId,
                            int totalPrice, LocalDateTime paymentDate, boolean isPaid, boolean isRefunded) {
        this.order = new PaymentSearchOrderDto(orderId, address, phoneNumber);
        this.payment = new PaymentSearchPaymentDto(paymentId, totalPrice, paymentDate, isPaid, isRefunded);
    }
}

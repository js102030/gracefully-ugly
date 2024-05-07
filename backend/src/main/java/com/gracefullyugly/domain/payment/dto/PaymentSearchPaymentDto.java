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
public class PaymentSearchPaymentDto {

    private Long paymentId;

    private String tid;

    private int totalPrice;

    private LocalDateTime paymentDate;

    private boolean isPaid;

    private boolean isRefunded;
}

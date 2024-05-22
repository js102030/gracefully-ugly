package com.gracefullyugly.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefundInfo {

    private Long userId;
    private Long orderId;

}

package com.gracefullyugly.domain.payment.dto;

import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSearchResultDTO {

    private PaymentSearchDTO paymentInfo;

    private List<OrderItemInfoResponse> orderItemList;
}

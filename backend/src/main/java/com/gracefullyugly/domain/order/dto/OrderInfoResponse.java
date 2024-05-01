package com.gracefullyugly.domain.order.dto;

import com.gracefullyugly.domain.order.entity.Order;
import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import com.gracefullyugly.domain.payment.entity.Payment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoResponse {

    private Order order;
    private String nickname;
    private Payment payment;
    private List<OrderItemInfoResponse> orderItemList;
}

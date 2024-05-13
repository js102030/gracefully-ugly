package com.gracefullyugly.domain.payment.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.payment.dto.PaymentSearchDTO;
import com.gracefullyugly.domain.payment.dto.PaymentSearchListResponse;
import com.gracefullyugly.domain.payment.dto.PaymentSearchResultDTO;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PaymentSearchService {

    private OrderItemRepository orderItemRepository;
    private PaymentRepository paymentRepository;

    public PaymentSearchListResponse getPaymentList(Long userId) {
        List<PaymentSearchDTO> paymentSearchDTOList = paymentRepository.findPaymentsByUserId(userId);
        List<PaymentSearchResultDTO> result = paymentSearchDTOList.stream()
                .map(paymentSearchDTO -> {
                    List<OrderItemInfoResponse> orderItemInfo = orderItemRepository.getOrderItemListByOrderId(
                            paymentSearchDTO.getOrder().getOrderId());

                    return new PaymentSearchResultDTO(paymentSearchDTO, orderItemInfo);
                })
                .toList();

        return new PaymentSearchListResponse(result);
    }

    public PaymentSearchResultDTO getPayment(Long userId, Long orderId) {
        PaymentSearchDTO paymentSearchDTO = paymentRepository.findPaymentByUserIdAndOrderId(userId, orderId)
                .orElseThrow(() -> new NotFoundException("결제 정보가 없습니다."));
        List<OrderItemInfoResponse> orderItemInfo = orderItemRepository.getOrderItemListByOrderId(
                paymentSearchDTO.getOrder().getOrderId());

        return new PaymentSearchResultDTO(paymentSearchDTO, orderItemInfo);
    }
}

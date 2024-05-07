package com.gracefullyugly.domain.payment.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.payment.dto.PaymentSearchDTO;
import com.gracefullyugly.domain.payment.dto.PaymentSearchListResponse;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PaymentSearchService {

    private PaymentRepository paymentRepository;

    public PaymentSearchListResponse getPaymentList(Long userId) {
        return PaymentSearchListResponse.builder()
                .paymentList(paymentRepository.findPaymentsByUserId(userId))
                .build();
    }

    public PaymentSearchDTO getPayment(Long userId, Long orderId) {
        return paymentRepository.findPaymentByUserIdAndOrderId(userId, orderId)
                .orElseThrow(() -> new NotFoundException("결제 정보가 없습니다."));
    }
}

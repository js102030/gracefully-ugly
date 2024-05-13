package com.gracefullyugly.domain.payment.controller;

import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.payment.dto.PaymentSearchListResponse;
import com.gracefullyugly.domain.payment.dto.PaymentSearchResultDTO;
import com.gracefullyugly.domain.payment.service.PaymentSearchService;
import com.gracefullyugly.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private PaymentService paymentService;
    private PaymentSearchService paymentSearchService;

    @GetMapping("/payment")
    public ResponseEntity<PaymentSearchListResponse> getPaymentList(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId){
        return ResponseEntity.ok(paymentSearchService.getPaymentList(userId));
    }

    @GetMapping("/payment/{orderId}")
    public ResponseEntity<PaymentSearchResultDTO> getPayment(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(paymentSearchService.getPayment(userId, orderId));
    }

    @PostMapping("/payment")
    public ResponseEntity<String> readyKakaoPay(@Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
                                @Valid @RequestBody OrderResponse order) {
        return ResponseEntity.ok(paymentService.readyKakaoPay(order));
    }

    @PutMapping("/payment/kakaopay/refund/{orderId}")
    public ResponseEntity<Void> refundKakaoPay(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("orderId") Long orderId) {
        paymentService.refundKakaoPay(userId, orderId);
        return ResponseEntity.ok().build();
    }
}

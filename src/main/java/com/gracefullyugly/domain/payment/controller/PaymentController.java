package com.gracefullyugly.domain.payment.controller;

import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.payment.dto.PaymentSearchListResponse;
import com.gracefullyugly.domain.payment.dto.PaymentSearchResultDTO;
import com.gracefullyugly.domain.payment.service.PaymentSearchService;
import com.gracefullyugly.domain.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
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

@Tag(name="상품 결제 관리")
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private PaymentService paymentService;
    private PaymentSearchService paymentSearchService;

    @Operation(summary = "회원 결제 정보 조회", description = "회원의 결제 정보를 조회함")
    @GetMapping("/payment")
    public ResponseEntity<PaymentSearchListResponse> getPaymentList(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId) {
        return ResponseEntity.ok(paymentSearchService.getPaymentList(userId));
    }

    @Operation(summary = "결제한 상품 조회", description = "구매자가 결제한 상품의 주문 정보를 조회함")
    @GetMapping("/payment/{orderId}")
    public ResponseEntity<PaymentSearchResultDTO> getPayment(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok(paymentSearchService.getPayment(userId, orderId));
    }

    @Operation(summary = "상품 주문 결제 하기", description = "카카오페이 api를 사용해 상품을 결제함")
    @PostMapping("/payment")
    public ResponseEntity<String> readyKakaoPay(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @Valid @RequestBody OrderResponse order) {
        return ResponseEntity.ok(paymentService.readyKakaoPay(order));
    }

    @Operation(summary = "상품 주문 환불", description = "구매자가 결제한 상품 주문을 환불함")
    @PutMapping("/payment/kakaopay/refund/{orderId}")
    public ResponseEntity<Void> refundKakaoPay(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("orderId") Long orderId) throws MessagingException, UnsupportedEncodingException {
        paymentService.refundKakaoPay(userId, orderId);
        return ResponseEntity.ok().build();
    }
}

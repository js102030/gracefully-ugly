package com.gracefullyugly.domain.payment.controller;

import com.gracefullyugly.domain.payment.dto.KakaoPayApproveResponse;
import com.gracefullyugly.domain.payment.dto.PaymentSearchListResponse;
import com.gracefullyugly.domain.payment.service.PaymentSearchService;
import com.gracefullyugly.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PaymentViewController {

    private PaymentService paymentService;
    private PaymentSearchService paymentSearchService;

    @GetMapping("/payment")
    public String getPaymentList(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            Model model) {
        PaymentSearchListResponse response = paymentSearchService.getPaymentList(userId);
        model.addAttribute("PaymentSearchListResponse", response);

        return "purchase_history";
    }

    @GetMapping("/payment/kakaopay/success/{userId}/{orderId}")
    public String approveKakaoPay(@PathVariable("userId") Long userId,
                                  @PathVariable("orderId") Long orderId,
                                  @RequestParam("pg_token") String pgToken,
                                  Model model) {
        KakaoPayApproveResponse response = paymentService.approveKakaoPay(userId, orderId, pgToken);
        model.addAttribute("KakaoPayApproveResponse", response);

        return "complete-payment";
    }

    @GetMapping("/payment/cancel")
    public String cancelKakaoPay() {

        return "payment-cancel";
    }

    @GetMapping("/payment/fail")
    public String failKakaoPay() {

        return "payment-fail";
    }
}

package com.gracefullyugly.domain.payment.controller;

import com.gracefullyugly.domain.payment.dto.KakaoPayApproveResponse;
import com.gracefullyugly.domain.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class PaymentViewController {

    private PaymentService paymentService;

    @GetMapping("/payment/kakaopay/success/{userId}/{orderId}")
    public String approveKakaoPay(@PathVariable("userId") Long userId,
                                  @PathVariable("orderId") Long orderId,
                                  @RequestParam("pg_token") String pgToken,
                                  Model model) {
        KakaoPayApproveResponse response = paymentService.approveKakaoPay(userId, orderId, pgToken);
        model.addAttribute("KakaoPayApproveResponse", response);

        return "/complete-payment";
    }

    // ToDo: 카카오 페이 결제 취소 시 보여줄 페이지 제작 필요
    @GetMapping("/payment/cancel")
    public String cancelKakaoPay() {

        return "/payment-cancel";
    }

    @GetMapping("/payment/fail")
    public String failKakaoPay() {

        return "/payment-fail";
    }
}

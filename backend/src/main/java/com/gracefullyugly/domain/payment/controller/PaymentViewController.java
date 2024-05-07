package com.gracefullyugly.domain.payment.controller;

import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class PaymentViewController {

    private PaymentService paymentService;

    @PostMapping("/payment")
    public String readyKakaoPay(@Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
                                @Valid @RequestBody OrderResponse order) {
        return "redirect:" + paymentService.readyKakaoPay(order);
    }

    // ToDo: 카카오 페이 결제 취소 시 보여줄 페이지 제작 필요
    @GetMapping("/payment/cancel")
    public String cancelKakaoPay() {

        return "";
    }
}
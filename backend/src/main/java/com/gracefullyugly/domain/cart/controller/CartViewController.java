package com.gracefullyugly.domain.cart.controller;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class CartViewController {

    private final CartService cartService;

    /**
     * 찜 목록을 조회하는 API입니다.
     */
    @GetMapping("/cart")
    public String getCartList(@Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
                              Model model) {
        List<CartListResponse> cartListResponses = cartService.getCartList(userId);
        model.addAttribute("CartList", cartListResponses);

        return "/cart-list";
    }
}

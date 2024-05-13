package com.gracefullyugly.domain.cart.controller.api;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    /**
     * 찜 목록을 조회하는 API입니다.
     */
    @GetMapping("/cart")
    public ResponseEntity<List<CartListResponse>> getCartList(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId) {
        return ResponseEntity.ok(cartService.getCartList(userId));
    }
}

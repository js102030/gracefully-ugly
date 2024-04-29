package com.gracefullyugly.domain.cart.controller.api;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    /**
     *      찜 목록을 조회하는 API입니다.
     */
    @GetMapping("/cart/{userId}")
    public ResponseEntity<List<CartListResponse>> getCartList(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(cartService.getCartList(userId));
    }
}

package com.gracefullyugly.domain.cart.controller.api;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="찜 목록 조회")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "찜 목록 조회", description = "찜한 상품 목록 조회하기")
    @GetMapping("/cart")
    public ResponseEntity<List<CartListResponse>> getCartList(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId) {
        return ResponseEntity.ok(cartService.getCartList(userId));
    }
}

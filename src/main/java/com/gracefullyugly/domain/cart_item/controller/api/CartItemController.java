package com.gracefullyugly.domain.cart_item.controller.api;

import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.dto.CartItemResponse;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "찜 목록 추가 & 삭제")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartItemController {

    private final CartItemService cartItemService;

    @Operation(summary = "찜 목록 상품 추가", description = "찜 목록에 상품 추가하기")
    @PostMapping("/cart/{itemId}")
    public ResponseEntity<CartItemResponse> addCartItem(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable(name = "itemId") Long itemId, @Valid @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartItemService.addCartItem(userId, itemId, request));
    }

    @Operation(summary = "찜 목록 상품 삭제", description = "찜 목록에 있는 상품 삭제하기")
    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<CartItemResponse> deleteCartItem(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable(name = "cartItemId") Long cartItemId) {
        return ResponseEntity.ok(cartItemService.deleteCartItem(userId, cartItemId));
    }
}

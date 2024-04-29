package com.gracefullyugly.domain.cart_item.service;

import com.gracefullyugly.domain.cart.entity.Cart;
import com.gracefullyugly.domain.cart.repository.CartMapper;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.dto.CartItemResponse;
import com.gracefullyugly.domain.cart_item.repository.CartItemMapper;
import com.gracefullyugly.domain.cart_item.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartItemResponse addCartItem(Long userId, Long itemId, AddCartItemRequest request) {
        if (request.getItemCount() == null || request.getItemCount() <= 0) {
            return CartItemResponse.builder()
                .message("수량을 선택해주세요.")
                .build();
        }

        Long itemCount = request.getItemCount();

        Optional<Cart> userCart = cartRepository.findCartByUserId(userId);

        if (userCart.isEmpty()) {
            if (cartMapper.createNewCart(userId) != 1) {
                return CartItemResponse.builder()
                    .message("회원 정보가 존재하지 않습니다.")
                    .build();
            }
        }

        Long cartId = userCart.orElseThrow().getId();

        String message = cartItemMapper.addCartItem(cartId, itemId, itemCount) >= 1 ? "해당 상품이 찜 목록에 추가되었습니다." : "찜 목록 추가 중 문제가 발생했습니다.";

        return CartItemResponse.builder()
            .message(message)
            .build();
    }

    public CartItemResponse deleteCartItem(Long userId, Long itemId) {
        Optional<Cart> userCart = cartRepository.findCartByUserId(userId);

        if (userCart.isEmpty()) {
            return CartItemResponse.builder()
                .message("찜 목록이 아직 생성되지 않았습니다.")
                .build();
        }

        String message = cartItemMapper.deleteCartItem(userCart.get().getId(), itemId) >= 1 ? "해당 상품이 찜 목록에서 삭제되었습니다." : "삭제 중 문제가 발생했습니다.";

        return CartItemResponse.builder()
            .message(message)
            .build();
    }
}

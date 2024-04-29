package com.gracefullyugly.domain.cart.service;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;

    public List<CartListResponse> getCartList(Long userId) {
        return cartRepository.selectAllCartItems(userId);
    }
}
package com.gracefullyugly.domain.cart.repository;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.entity.Cart;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {

    Long createNewCart(Long userId);

    Optional<Cart> selectCartByUserId(Long userId);

    List<CartListResponse> selectAllCartItems(Long userId);
}

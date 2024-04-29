package com.gracefullyugly.domain.cart.repository;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {

    List<CartListResponse> selectAllCartItems(Long userId);
    Integer addCartItem();
    Integer deleteCartItem(Long itemId);
}

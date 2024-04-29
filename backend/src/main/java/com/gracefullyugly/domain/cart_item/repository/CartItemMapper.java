package com.gracefullyugly.domain.cart_item.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartItemMapper {

    Integer addCartItem(@Param("cartId") Long cartId, @Param("itemId") Long itemId, @Param("itemCount") Long itemCount);

    Integer deleteCartItem(@Param("cartId") Long cartId, @Param("itemId") Long itemId);
}

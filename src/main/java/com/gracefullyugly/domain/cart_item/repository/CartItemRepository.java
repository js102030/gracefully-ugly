package com.gracefullyugly.domain.cart_item.repository;

import com.gracefullyugly.domain.cart_item.entity.CartItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query(value =
            "INSERT INTO cart_item(`cart_id`, `item_id`, `item_count`, `created_date`, `last_modified_date`) "
                    + "VALUES (:cartId, :itemId, :itemCount, NOW(), NOW())",
            nativeQuery = true)
    @Modifying
    Integer addCartItem(@Param("cartId") Long cartId, @Param("itemId") Long itemId, @Param("itemCount") Long itemCount);

    @Query(value =
            "DELETE FROM cart_item " +
                    "WHERE cart_id = :cartId AND cart_item_id = :cartItemId",
            nativeQuery = true)
    @Modifying
    Integer deleteCartItem(@Param("cartId") Long cartId, @Param("cartItemId") Long cartItemId);

    Boolean existsCartItemByCartIdAndItemId(Long cartId, Long itemId);
}

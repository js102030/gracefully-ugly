package com.gracefullyugly.domain.cart.repository;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.entity.Cart;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT CI.item_count, I.*"
        + " FROM cart AS C"
        + " LEFT OUTER JOIN cart_item AS CI ON C.cart_id = CI.cart_id"
        + " LEFT OUTER JOIN item AS I ON CI.item_id = I.item_id"
        + " WHERE C.user_id = :userId", nativeQuery = true)
    List<CartListResponse> selectAllCartItems(@Param("userId") Long userId);

    Optional<Cart> findCartByUserId(Long userId);

    @Query(value = "INSERT INTO cart(`user_id`, `created_date`, `last_modified_date`)"
        + " SELECT user_id, NOW(), NOW()"
        + " FROM users"
        + " WHERE user_id = :userId", nativeQuery = true)
    @Modifying
    Integer createNewCart(Long userId);

    boolean existsCartByUserId(Long userId);
}

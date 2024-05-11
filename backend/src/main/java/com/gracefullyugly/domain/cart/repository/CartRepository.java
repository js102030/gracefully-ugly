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

    @Query(value = """
            SELECT CI.cart_item_id AS cartItemId, CI.item_count AS itemCount, I.item_id AS itemId, I.name AS name, 
                   I.price AS price, I.category_id AS categoryId, I.closed_date AS closeDate, img.url AS imageUrl
            FROM cart AS C
            LEFT OUTER JOIN cart_item AS CI ON C.cart_id = CI.cart_id
            LEFT OUTER JOIN item AS I ON CI.item_id = I.item_id AND I.is_deleted = false
            LEFT OUTER JOIN image img ON I.item_id = img.item_id AND img.is_deleted = false
            WHERE C.user_id = :userId
            """, nativeQuery = true)
    List<CartListResponse> selectAllCartItemsWithImages(@Param("userId") Long userId);


    Optional<Cart> findCartByUserId(Long userId);

    @Query(value =
            "INSERT INTO cart(`user_id`, `created_date`, `last_modified_date`) "
                    + "SELECT user_id, NOW(), NOW() "
                    + "FROM users "
                    + "WHERE user_id = :userId",
            nativeQuery = true)
    @Modifying
    Integer createNewCart(Long userId);

    boolean existsCartByUserId(Long userId);
}

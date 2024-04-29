package com.gracefullyugly.domain.cart.repository;

import com.gracefullyugly.domain.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findCartByUserId(Long userId);

    @Query(value = "INSERT INTO cart(`user_id`, `created_date`, `last_modified_date`)"
        + " SELECT user_id, NOW(), NOW()"
        + " FROM users"
        + " WHERE user_id = :userId", nativeQuery = true)
    @Modifying
    Integer createNewCart(Long userId);

    boolean existsCartByUserId(Long userId);
}

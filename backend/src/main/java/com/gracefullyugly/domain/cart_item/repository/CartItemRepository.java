package com.gracefullyugly.domain.cart_item.repository;

import com.gracefullyugly.domain.cart_item.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}

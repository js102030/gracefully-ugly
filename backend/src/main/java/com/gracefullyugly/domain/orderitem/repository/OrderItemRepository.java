package com.gracefullyugly.domain.orderitem.repository;

import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query(value =
        "SELECT OI.item_id AS itemId, I.name AS name, I.price AS price, OI.quantity AS quantity "
      + "FROM orders_item AS OI "
      + "LEFT OUTER JOIN item AS I ON OI.item_id = I.item_id "
      + "WHERE OI.order_id = :orderId",
        nativeQuery = true)
    List<OrderItemInfoResponse> findAllByOrderId(Long orderId);
}

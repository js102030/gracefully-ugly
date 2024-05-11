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
        "SELECT new com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse(I.id, I.categoryId, I.name, I.price, OI.quantity) AS item "
      + "FROM OrderItem AS OI "
      + "LEFT OUTER JOIN Item AS I ON OI.itemId = I.id "
      + "WHERE OI.ordersId = :orderId ")
    List<OrderItemInfoResponse> getOrderItemListByOrderId(Long orderId);

    List<OrderItem> findAllByOrdersId(Long orderId);
}

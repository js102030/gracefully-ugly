package com.gracefullyugly.domain.orderitem.repository;

import com.gracefullyugly.domain.orderitem.dto.OrderItemInfo;
import com.gracefullyugly.domain.orderitem.dto.OrderItemInfoResponse;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import java.util.List;
import org.apache.ibatis.annotations.Param;
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

    @Query(value =
            "SELECT I.name AS itemName, " +
                    "I.production_place AS productionPlace, " +
                    "OI.quantity AS orderItemQuantity," +
                    "I.price AS itemPrice, " +
                    "O.address AS address, " +
                    "O.created_date AS orderCreatedDate " +
                    "FROM item I " +
                    "LEFT JOIN orders_item OI ON I.item_id = OI.item_id " +
                    "LEFT JOIN orders O ON OI.orders_id = O.order_id " +
                    "WHERE O.order_id = :orderId", nativeQuery = true)
    OrderItemInfo findOrderItemInfo(@Param("orderId") Long orderId);
}

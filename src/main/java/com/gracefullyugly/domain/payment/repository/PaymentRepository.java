package com.gracefullyugly.domain.payment.repository;

import com.gracefullyugly.domain.payment.dto.PaymentSearchDTO;
import com.gracefullyugly.domain.payment.entity.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT new com.gracefullyugly.domain.payment.dto.PaymentSearchDTO(O.id, O.address, O.phoneNumber, P.id, P.totalPrice, P.lastModifiedDate, P.isPaid, P.isRefunded) "
            + "FROM Payment AS P "
            + "LEFT OUTER JOIN Order AS O ON P.orderId = O.id "
            + "WHERE O.userId = :userId "
            + "ORDER BY P.lastModifiedDate DESC "
            + "LIMIT 30")
    List<PaymentSearchDTO> findPaymentsByUserId(Long userId);

    @Query("SELECT new com.gracefullyugly.domain.payment.dto.PaymentSearchDTO(O.id, O.address, O.phoneNumber, P.id, P.totalPrice, P.lastModifiedDate, P.isPaid, P.isRefunded) "
            + "FROM Payment AS P "
            + "LEFT OUTER JOIN Order AS O ON P.orderId = O.id "
            + "WHERE O.userId = :userId AND P.orderId = :orderId")
    Optional<PaymentSearchDTO> findPaymentByUserIdAndOrderId(Long userId, Long orderId);

    Optional<Payment> findPaymentByOrderId(Long orderId);

    Optional<Payment> findByOrderId(Long orderId);

    @Query(value =
            "SELECT COUNT(*) "
                    + "FROM payment AS P "
                    + "LEFT OUTER JOIN orders AS O ON P.order_id = O.order_id "
                    + "WHERE O.user_id = :userId AND P.is_paid = true AND P.is_refunded = false ",
            nativeQuery = true)
    Integer getBuyCountByUserId(Long userId);

    @Modifying
    @Query(value = "UPDATE payment p " +
            "SET p.is_refunded = true " +
            "WHERE p.id IN (" +
            "    SELECT p.id " +
            "    FROM payment p " +
            "    INNER JOIN group_buy_user gbu ON p.order_id = gbu.order_id " +
            "    INNER JOIN group_buy gb ON gbu.group_buy_id = gb.group_buy_id " +
            "    WHERE gb.groupBuyStatus = 'CANCELLED' " +
            "    AND p.is_refunded = false" +
            ")",
            nativeQuery = true)
    int updatePaymentsToRefundedForCancelledGroupBuys();


}

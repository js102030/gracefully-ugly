package com.gracefullyugly.domain.payment.repository;

import com.gracefullyugly.domain.payment.dto.PaymentSearchDTO;
import com.gracefullyugly.domain.payment.dto.RefundInfo;
import com.gracefullyugly.domain.payment.entity.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Query("SELECT new com.gracefullyugly.domain.payment.dto.RefundInfo(o.userId, gbu.orderId) " +
            "FROM GroupBuyUser gbu " +
            "JOIN Order o ON o.id = gbu.orderId " +
            "JOIN Payment p ON p.orderId = gbu.orderId " +
            "JOIN GroupBuy gb ON gbu.groupBuyId = gb.id " +
            "WHERE gb.groupBuyStatus = 'CANCELLED' AND p.isRefunded = false")
    List<RefundInfo> findRefundablePayments();

}

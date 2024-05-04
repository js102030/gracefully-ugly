package com.gracefullyugly.domain.payment.repository;

import com.gracefullyugly.domain.payment.dto.PaymentSearchDTO;
import com.gracefullyugly.domain.payment.entity.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT new com.gracefullyugly.domain.payment.dto.PaymentSearchOrderDto(O.id, O.address, O.phoneNumber) AS order, "
            + "new com.gracefullyugly.domain.payment.dto.PaymentSearchItemDto(I.id, I.categoryId, I.name, I.price, OI.quantity) AS item, "
            + "new com.gracefullyugly.domain.payment.dto.PaymentSearchPaymentDto(P.id, P.totalPrice, P.createdDate, P.isPaid, P.isRefunded) AS payment "
            + "FROM Payment AS P "
            + "LEFT OUTER JOIN Order AS O ON P.orderId = O.id "
            + "LEFT OUTER JOIN OrderItem AS OI ON O.id = OI.orderId "
            + "LEFT OUTER JOIN Item AS I ON OI.itemId = I.id "
            + "WHERE O.userId = :userId")
    List<PaymentSearchDTO> findPaymentsByUserId(Long userId);

    @Query("SELECT new com.gracefullyugly.domain.payment.dto.PaymentSearchOrderDto(O.id, O.address, O.phoneNumber) AS order, "
            + "new com.gracefullyugly.domain.payment.dto.PaymentSearchItemDto(I.id, I.categoryId, I.name, I.price, OI.quantity) AS item, "
            + "new com.gracefullyugly.domain.payment.dto.PaymentSearchPaymentDto(P.id, P.totalPrice, P.createdDate, P.isPaid, P.isRefunded) AS payment "
            + "FROM Payment AS P "
            + "LEFT OUTER JOIN Order AS O ON P.orderId = O.id "
            + "LEFT OUTER JOIN OrderItem AS OI ON O.id = OI.orderId "
            + "LEFT OUTER JOIN Item AS I ON OI.itemId = I.id "
            + "WHERE O.userId = :userId AND P.orderId = :orderId")
    Optional<PaymentSearchDTO> findPaymentByUserIdAndOrderId(Long userId, Long orderId);

    Optional<Payment> findByOrderId(Long orderId);
}

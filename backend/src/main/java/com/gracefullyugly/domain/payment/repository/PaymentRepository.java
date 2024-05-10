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
}

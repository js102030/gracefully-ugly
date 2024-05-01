package com.gracefullyugly.domain.payment.repository;

import com.gracefullyugly.domain.payment.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Boolean existsByOrderId(Long orderId);

    Optional<Payment> findByOrderId(Long orderId);
}

package com.gracefullyugly.domain.payment.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    private Long orderId;

    private String tid;

    private int totalPrice;

    private boolean isPaid;

    private boolean isRefunded;

    public Payment(Long orderId, String tid, int totalPrice) {
        this.orderId = orderId;
        this.tid = tid;
        this.totalPrice = totalPrice;
        this.isPaid = false;
        this.isRefunded = false;
    }

    public Payment updateIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
        return this;
    }

    public Payment updateIsRefunded(boolean isRefunded) {
        this.isRefunded = isRefunded;
        return this;
    }
}

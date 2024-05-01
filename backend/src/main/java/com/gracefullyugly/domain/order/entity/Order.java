package com.gracefullyugly.domain.order.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "orders")
@DynamicUpdate
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Long userId;

    private String address;

    private String phoneNumber;

    public Order(Long userId, String address, String phoneNumber) {
        this.userId = userId;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Order updateAddress(String address) {
        this.address = address;
        return this;
    }

    public Order updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
}

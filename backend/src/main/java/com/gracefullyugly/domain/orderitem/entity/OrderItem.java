package com.gracefullyugly.domain.orderitem.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "orders_item")
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_item_id")
    private Long id;

    private Long itemId;

    private Long ordersId;

    private int quantity;

    public OrderItem(Long itemId, Long orderId, int quantity) {
        this.itemId = itemId;
        this.ordersId = orderId;
        this.quantity = quantity;
    }
}

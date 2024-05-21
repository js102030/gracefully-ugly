package com.gracefullyugly.domain.groupbuyuser.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class GroupBuyUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_buy_user_id")
    private Long id;

    private Long groupBuyId;

    private Long orderId;

    private LocalDateTime joinDate;

    private int quantity;

    public GroupBuyUser(Long groupBuyId, Long orderId, LocalDateTime joinDate, int quantity) {
        this.groupBuyId = groupBuyId;
        this.orderId = orderId;
        this.joinDate = joinDate;
        this.quantity = quantity;
    }
}

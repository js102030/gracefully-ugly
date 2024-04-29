package com.gracefullyugly.domain.groupbuy.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
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
public class GroupBuy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_buy_id")
    private Long id;

    private Long ItemId;

    private GroupBuyStatus groupBuyStatus;

    private LocalDateTime endDate;

}
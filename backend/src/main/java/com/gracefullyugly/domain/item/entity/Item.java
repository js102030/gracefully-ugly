package com.gracefullyugly.domain.item.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private Long userId;

    private Long categoryId;

    private String name;

    private String description;

    private int price;

    private int totalSalesUnit;

    private int minUnitWeight;

    private int minGroupBuyWeight;

    private String productionPlace;

    private boolean isDeleted;

    private boolean isClosed;

}

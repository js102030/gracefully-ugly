package com.gracefullyugly.domain.item.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    private Category categoryId;

    private String name;

    private String description;

    private int price;

    private int totalSalesUnit;

    private int minUnitWeight;

    private int minGroupBuyWeight;

    private String productionPlace;

    private LocalDateTime closedDate;

    private boolean isDeleted;

    // TODO 이미지 추가

    @Builder
    public Item(Long id, Long userId, Category categoryId, String name, String description, int price, int totalSalesUnit,
                int minUnitWeight, int minGroupBuyWeight, String productionPlace, LocalDateTime closedDate) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.totalSalesUnit = totalSalesUnit;
        this.minUnitWeight = minUnitWeight;
        this.minGroupBuyWeight = minGroupBuyWeight;
        this.productionPlace = productionPlace;
        this.closedDate = closedDate;
    }

    public UpdateAddressDto updateDescription(String description) {
        this.description = description;
        return new UpdateAddressDto(description);
    }

    public void delete() {
        this.isDeleted = true;
    }


}

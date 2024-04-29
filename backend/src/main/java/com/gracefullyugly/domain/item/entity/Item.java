package com.gracefullyugly.domain.item.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.category.entity.Category;
import com.gracefullyugly.domain.item.dto.ItemRequestDto;
import com.gracefullyugly.domain.item.dto.ItemResponseDto;
import com.gracefullyugly.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter

public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String name;

    private String description;

    private int price;

    private int totalSalesUnit;

    private int minUnitWeight;

    private int minGroupBuyWeight;

    private String productionPlace;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime closedDate;

    private boolean isDeleted;

    private boolean isClosed;
    // todo 이미지 추가


    @Builder
    public Item(Long id, User user, Category category, String name, String description, int price, int totalSalesUnit, int minUnitWeight, int minGroupBuyWeight, String productionPlace, LocalDateTime closedDate, boolean isDeleted, boolean isClosed) {
        this.id = id;
        this.user = user;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.totalSalesUnit = totalSalesUnit;
        this.minUnitWeight = minUnitWeight;
        this.minGroupBuyWeight = minGroupBuyWeight;
        this.productionPlace = productionPlace;
        this.closedDate = closedDate;
        this.isDeleted = false;
        this.isClosed = false;
    }

    public ItemResponseDto toResponse() {
        return ItemResponseDto.builder()
                .id(id)
                .userName(user.getNickname())
                .name(name)
                .productionPlace(productionPlace)
                .categoryId(category.getId())
                .closedDate(closedDate)
                .createdDate(getCreatedDate())
                .lastModifiedDate(getLastModifiedDate())
                .name(name)
                .description(description)
                .price(price)
                .totalSalesUnit(totalSalesUnit)
                .minGroupBuyWeight(minGroupBuyWeight)
                .minUnitWeight(minUnitWeight)
                .description(description)
                .build();
    }

}

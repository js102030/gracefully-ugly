package com.gracefullyugly.domain.item.dto;

import com.gracefullyugly.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class ItemResponseDto {
    private Long id;
    private String userName;
    private String name;
    private String productionPlace;
    private Long categoryId;
    private LocalDateTime closedDate;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private int minUnitWeight;
    private int price;
    private int totalSalesUnit;
    private int minGroupBuyWeight;
    private String description;
    // todo 이미지 추가

    public ItemResponseDto(Item item) {
        id = item.getId();
        userName = item.getUser().getNickname();
        name = item.getName();
        productionPlace = item.getProductionPlace();
        categoryId = item.getCategory().getId();
        closedDate = item.getClosedDate();
        createdDate = item.getCreatedDate();
        lastModifiedDate = item.getLastModifiedDate();
        minUnitWeight = item.getMinUnitWeight();
        price = item.getPrice();
        totalSalesUnit = item.getTotalSalesUnit();
        minGroupBuyWeight = item.getMinGroupBuyWeight();
        description = item.getDescription();
    }
}

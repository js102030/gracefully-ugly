package com.gracefullyugly.domain.item.dto;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemResponse {

    private Long id;
    private String name;
    private String productionPlace;
    private Category categoryId;
    private LocalDateTime closedDate;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private int minUnitWeight;
    private int price;
    private int totalSalesUnit;
    private int minGroupBuyWeight;
    private String description;
    // TODO 이미지 추가

    public ItemResponse(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.productionPlace = item.getProductionPlace();
        this.categoryId = item.getCategoryId();
        this.closedDate = item.getClosedDate();
        this.createdDate = item.getCreatedDate();
        this.lastModifiedDate = item.getLastModifiedDate();
        this.minUnitWeight = item.getMinUnitWeight();
        this.price = item.getPrice();
        this.totalSalesUnit = item.getTotalSalesUnit();
        this.minGroupBuyWeight = item.getMinGroupBuyWeight();
        this.description = item.getDescription();
    }
}

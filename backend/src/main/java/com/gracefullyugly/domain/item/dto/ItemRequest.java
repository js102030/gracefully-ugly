package com.gracefullyugly.domain.item.dto;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    private String name;
    private String productionPlace;
    private Category categoryId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime closedDate;

    private int minUnitWeight;
    private int price;
    private int totalSalesUnit;
    private int minGroupBuyWeight;
    private String description;

    public Item toEntity(Long userId) {

        return Item.builder()
                .userId(userId)
                .categoryId(categoryId)
                .name(name)
                .productionPlace(productionPlace)
                .closedDate(closedDate)
                .minUnitWeight(minUnitWeight)
                .price(price)
                .totalSalesUnit(totalSalesUnit)
                .minGroupBuyWeight(minGroupBuyWeight)
                .description(description)
                .build();

    }
}

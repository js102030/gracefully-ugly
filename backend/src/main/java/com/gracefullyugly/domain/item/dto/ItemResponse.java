package com.gracefullyugly.domain.item.dto;

import java.time.LocalDateTime;

import com.gracefullyugly.domain.item.enumtype.Category;
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

}

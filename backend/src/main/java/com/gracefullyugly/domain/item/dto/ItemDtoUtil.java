package com.gracefullyugly.domain.item.dto;

import com.gracefullyugly.domain.item.entity.Item;

public class ItemDtoUtil {

    public static ItemResponse itemToItemResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .userId(item.getUserId())
                .name(item.getName())
                .productionPlace(item.getProductionPlace())
                .categoryId(item.getCategoryId())
                .closedDate(item.getClosedDate())
                .createdDate(item.getCreatedDate())
                .lastModifiedDate(item.getLastModifiedDate())
                .minUnitWeight(item.getMinUnitWeight())
                .price(item.getPrice())
                .totalSalesUnit(item.getTotalSalesUnit())
                .minGroupBuyWeight(item.getMinGroupBuyWeight())
                .description(item.getDescription())
                .build();
        // TODO 이미지 추가
    }

}

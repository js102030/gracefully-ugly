package com.gracefullyugly.domain.item.dto;

import com.gracefullyugly.domain.category.entity.Category;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    private String name;
    private String productionPlace;
    private Long categoryId;
    private LocalDateTime closedDate;
    private int minUnitWeight;
    private int price;
    private int minGroupBuyWeight;
    private String description;
    // todo 이미지 추가

    public Item toEntity(User user ,Category category) {
        return Item.builder()
                .name(name)
                .productionPlace(productionPlace)
                .category(category)
                .user(user)
                .closedDate(closedDate)
                .minUnitWeight(minUnitWeight)
                .price(price)
                .minGroupBuyWeight(minGroupBuyWeight)
                .description(description)
                .build();
    }
}

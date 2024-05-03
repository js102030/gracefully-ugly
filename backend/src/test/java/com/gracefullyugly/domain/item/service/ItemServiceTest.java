package com.gracefullyugly.domain.item.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionRequest;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemSearchService itemSearchService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("판매글 수정 테스트")
    void updateDescriptionTest() {
        // GIVEN
        Item savedItem = itemRepository.save(Item.builder()
                .userId(1L)
                .categoryId(Category.FRUIT)
                .name("테스트")
                .productionPlace("테스트")
                .closedDate(null)
                .minUnitWeight(1)
                .price(1000)
                .totalSalesUnit(1)
                .minGroupBuyWeight(1)
                .description("테스트")
                .build());

        String newDescription = "업데이트 테스트";

        // WHEN
        UpdateDescriptionRequest updateDescriptionRequest = UpdateDescriptionRequest.builder()
                .description(newDescription)
                .build();

        ItemResponse updatedDto = itemService.updateDescription(savedItem.getId(), savedItem.getUserId(),
                updateDescriptionRequest);

        // THEN
        Assertions.assertEquals(newDescription, updatedDto.getDescription());
    }

    @Test
    @DisplayName("판매글 삭제 테스트")
    void deletedById() {
        // GIVEN
        Item savedItem = itemRepository.save(Item.builder()
                .userId(1L)
                .categoryId(Category.FRUIT)
                .name("테스트")
                .productionPlace("테스트")
                .closedDate(null)
                .minUnitWeight(1)
                .price(1000)
                .totalSalesUnit(1)
                .minGroupBuyWeight(1)
                .description("테스트")
                .build());

        // WHEN
        itemService.deletedById(savedItem.getId(), savedItem.getUserId());
        Item deletedItem = itemSearchService.findById(savedItem.getId());

        // THEN
        assertTrue(deletedItem.isDeleted());
    }
}
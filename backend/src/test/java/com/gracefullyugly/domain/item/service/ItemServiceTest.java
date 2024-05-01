package com.gracefullyugly.domain.item.service;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionDto;
import com.gracefullyugly.domain.item.entity.Item;

import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemSearchService itemSearchService;

    @Autowired
    ItemRepository itemRepository;

    private Item existingItem;

    @BeforeEach
    void setUp() {
        existingItem = Item.builder()
                .id(1L)
                .name(SetupDataUtils.NAME)
                .productionPlace(SetupDataUtils.PRODUCTION_PLACE)
                .categoryId(SetupDataUtils.CATEGORY_ID)
                .closedDate(SetupDataUtils.CLOSED_DATE)
                .minUnitWeight(SetupDataUtils.MIN_UNIT_WEIGHT)
                .price(SetupDataUtils.PRICE)
                .totalSalesUnit(SetupDataUtils.TOTAL_SALES_UNIT)
                .minGroupBuyWeight(SetupDataUtils.MIN_GROUP_BUY_WEIGHT)
                .description(SetupDataUtils.DESCRIPTION)
                .build();
        itemRepository.save(existingItem);
    }

    @Test
    @DisplayName("판매글 수정 테스트")
    void updateDescriptionTest() {
        // GIVEN
        Long itemId = 1L;
        String newDescription = "업데이트 테스트";

        // WHEN
        UpdateDescriptionDto updateDescriptionDto = UpdateDescriptionDto.builder()
                .description(newDescription)
                .build();

        UpdateDescriptionDto updatedDto = itemService.updateDescription(itemId, updateDescriptionDto);

        // THEN
        Assertions.assertEquals(newDescription, updatedDto.getDescription());
    }

    @Test
    @DisplayName("판매글 삭제 테스트")
    void deletedById() {
        // GIVEN
        Long itemId = 1L;

        // WHEN
        itemService.deletedById(itemId);
        Item deletedItem = itemSearchService.findById(itemId);

        // THEN
        assertTrue(deletedItem.isDeleted());
    }
}
package com.gracefullyugly.domain.item.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionRequest;
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

    @Test
    @DisplayName("판매글 수정 테스트")
    void updateDescriptionTest() {
        // GIVEN
        Long itemId = 1L;
        Long userId = 1L;
        String newDescription = "업데이트 테스트";

        // WHEN
        UpdateDescriptionRequest updateDescriptionRequest = UpdateDescriptionRequest.builder()
                .description(newDescription)
                .build();

        ItemResponse updatedDto = itemService.updateDescription(itemId, userId, updateDescriptionRequest);

        // THEN
        Assertions.assertEquals(newDescription, updatedDto.getDescription());
    }

    @Test
    @DisplayName("판매글 삭제 테스트")
    void deletedById() {
        // GIVEN
        Long itemId = 1L;
        Long userId = 1L;

        // WHEN
        itemService.deletedById(itemId, userId);
        Item deletedItem = itemSearchService.findById(itemId);

        // THEN
        assertTrue(deletedItem.isDeleted());
    }
}
package com.gracefullyugly.domain.item.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.domain.cart.entity.Cart;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ItemSearchServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ItemSearchService itemSearchService;

    @Autowired
    ItemService itemService;

    @Autowired
    ObjectMapper objectMapper;

    private Item existingItem;

    // 판매글 여러개 제작할 때 사용
    private ItemRequest createItemRequest(String name, String productionPlace, Category categoryId, LocalDateTime closedDate,
                                          int minUnitWeight, int price, int totalSalesUnit, int minGroupBuyWeight, String description) {
        return new ItemRequest(name, productionPlace, categoryId, closedDate, minUnitWeight, price, totalSalesUnit, minGroupBuyWeight, description);
    }

    private ResultActions performPostRequest(ItemRequest itemRequest) throws Exception {
        String content = objectMapper.writeValueAsString(itemRequest);
        return mockMvc.perform(MockMvcRequestBuilders.post("/api/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    @Test
    @DisplayName("72시간 이내 마감임박 상품 목록 조회")
    void getImpendingItemsTest() {
        // GIVEN
        Long itemId1 = 1L;
        Long itemId2 = 2L;

        ItemRequest itemRequest1 = createItemRequest("테스트용 이름1", "테스트용 생산지1", Category.FRUIT,
                LocalDateTime.now().plusDays(1), 3, 7900, 20, 15, "테스트용 내용");
        ItemRequest itemRequest2 = createItemRequest("테스트용 이름2", "테스트용 생산지2", Category.VEGETABLE,
                LocalDateTime.now().plusDays(10), 5, 150000, 50, 30, "테스트용 내용2");

        ItemResponse item1 = itemService.save(itemId1, itemRequest1);
        ItemResponse item2 = itemService.save(itemId2, itemRequest2);

        // WHEN
        List<ItemResponse> impendingItems = itemSearchService.getImpendingItems();


        // THEN
        // 72시간 이상 남은 건 1개니까 결과는 하나여야됨
        Assertions.assertTrue(impendingItems.size() == 1);

        ItemResponse impendingItemResponse = impendingItems.get(0);
        Assertions.assertEquals(item1.getId(), impendingItemResponse.getId());
        Assertions.assertEquals(item1.getName(), impendingItemResponse.getName());
        Assertions.assertEquals(item1.getProductionPlace(), impendingItemResponse.getProductionPlace());
        Assertions.assertEquals(item1.getCategoryId(), impendingItemResponse.getCategoryId());
        Assertions.assertEquals(item1.getClosedDate(), impendingItemResponse.getClosedDate());
        Assertions.assertEquals(item1.getMinUnitWeight(), impendingItemResponse.getMinUnitWeight());
        Assertions.assertEquals(item1.getPrice(), impendingItemResponse.getPrice());
        Assertions.assertEquals(item1.getTotalSalesUnit(), impendingItemResponse.getTotalSalesUnit());
        Assertions.assertEquals(item1.getMinGroupBuyWeight(), impendingItemResponse.getMinGroupBuyWeight());
        Assertions.assertEquals(item1.getDescription(), impendingItemResponse.getDescription());

        // 72시간 이내 마감임박 상품 목록 조회 했으므로 다른 상품은 포함되지 않아야 함
        Assertions.assertFalse(impendingItems.stream().anyMatch(itemResponse ->
                !itemResponse.getId().equals(item1.getId())));

    }

    @Test
    @DisplayName("인기 상품 목록 조회")
    void GetPopularityItemsTest() {
        // GIVEN
        Long itemId1 = 1L;
        Long userId1 = 1L;

        Long itemId2 = 2L;
        Long userId2 = 2L;

        ItemRequest itemRequest1 = createItemRequest("테스트용 이름1", "테스트용 생산지1", Category.FRUIT,
                LocalDateTime.now().plusDays(5), 3, 7900, 20, 15, "테스트용 내용");
        ItemResponse itemResponse1 = itemService.save(itemId1, itemRequest1);

        // (더 인기 있는 상품)
        ItemRequest itemRequest2 = createItemRequest("테스트용 이름2", "테스트용 생산지2", Category.VEGETABLE,
                LocalDateTime.now().plusDays(7), 5, 150000, 50, 30, "테스트용 내용2");
        ItemResponse itemResponse2 = itemService.save(itemId2, itemRequest2);

        // Item 찜하기
        // todo 찜하기부터 구현

        // WHEN
        List<ItemResponse> popularityItems = itemSearchService.getPopularityItems();

        // THEN
        ItemResponse firstItem = popularityItems.get(0);
        Assertions.assertEquals(itemResponse2.getId(), firstItem.getId()); // 찜 개수가 높은 상품이 먼저 나와야 함

        ItemResponse secondItem = popularityItems.get(1);
        Assertions.assertEquals(itemResponse1.getId(), secondItem.getId());


    }
}
package com.gracefullyugly.domain.item.service;

import static com.gracefullyugly.testutil.SetupDataUtils.ADD_CART_ITEM_SUCCESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import com.gracefullyugly.domain.cart.service.CartService;
import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.dto.CartItemResponse;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@Transactional
class ItemSearchServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemSearchService itemSearchService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    CartService cartService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("72시간 이내 마감임박 상품 목록 조회")
    void getImpendingItemsTest() {
        // GIVEN
        Long itemId1 = 1L;
        ItemRequest itemRequest1 = ItemRequest.builder()
                .name("감자")
                .productionPlace("강원도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(1))
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        Long itemId2 = 2L;
        ItemRequest itemRequest2 = ItemRequest.builder()
                .name("고구마")
                .productionPlace("전라남도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(10))
                .minUnitWeight(1)
                .price(5000)
                .totalSalesUnit(50)
                .minGroupBuyWeight(20)
                .description("맛있는 고구마 ~ ")
                .build();

        ItemResponse item1 = itemService.save(itemId1, itemRequest1);
        ItemResponse item2 = itemService.save(itemId2, itemRequest2);

        // WHEN
        List<ItemResponse> impendingItems = itemSearchService.getImpendingItems();

        // THEN
        // 72시간 이내 남은 상품글은 1개(item2)니까 결과는 하나여야됨
        Assertions.assertEquals(1, impendingItems.size());

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
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();

        Long itemId1 = 3L;
        ItemRequest itemRequest1 = ItemRequest.builder()
                .name("감자")
                .productionPlace("강원도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(1))
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        Long itemId2 = 4L;
        ItemRequest itemRequest2 = ItemRequest.builder()
                .name("고구마")
                .productionPlace("전라남도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(10))
                .minUnitWeight(1)
                .price(5000)
                .totalSalesUnit(50)
                .minGroupBuyWeight(20)
                .description("맛있는 고구마 ~ ")
                .build();

        ItemResponse item1 = itemService.save(testUserId, itemRequest1);
        ItemResponse item2 = itemService.save(testUserId, itemRequest2);

        // 찜 상품에 데이터 추가
        AddCartItemRequest testItemCount1 = AddCartItemRequest.builder().itemCount(2L).build();
        AddCartItemRequest testItemCount2 = AddCartItemRequest.builder().itemCount(5L).build();

        CartItemResponse result1 = cartItemService.addCartItem(testUserId, itemId1, testItemCount1);
        CartItemResponse result2 = cartItemService.addCartItem(testUserId, itemId2, testItemCount2);

        List<ItemResponse> popularityItems = itemSearchService.getPopularityItems();

        // THEN
        System.out.println("리스트보기 :" + popularityItems);
        assertThat(popularityItems.size()).isEqualTo(2);
        assertThat(item2.getId()).isEqualTo(popularityItems.get(0).getId()); // 찜 개수가 높은 순으로 정렬되었는지 확인
        assertThat(item1.getId()).isEqualTo(popularityItems.get(1).getId());
    }

    @Test
    @DisplayName("상품 종류별 검색 목록 조회")
    void getCategoryItemsTest() {
        // GIVEN
        Long itemId1 =5L;
        ItemRequest itemRequest1 = ItemRequest.builder()
                .name("감자")
                .productionPlace("강원도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(1))
                .minUnitWeight(3)
                .price(7900)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("맛 좋은 감자")
                .build();

        Long itemId2 = 6L;
        ItemRequest itemRequest2 = ItemRequest.builder()
                .name("고구마")
                .productionPlace("전라남도")
                .categoryId(Category.VEGETABLE)
                .closedDate(LocalDateTime.now().plusDays(10))
                .minUnitWeight(1)
                .price(5000)
                .totalSalesUnit(50)
                .minGroupBuyWeight(20)
                .description("맛있는 고구마 ~ ")
                .build();

        Long itemId3 = 7L;
        ItemRequest itemRequest3 = ItemRequest.builder()
                .name("사과")
                .productionPlace("경상북도")
                .categoryId(Category.FRUIT)
                .closedDate(LocalDateTime.now().plusDays(10))
                .minUnitWeight(2)
                .price(8000)
                .totalSalesUnit(20)
                .minGroupBuyWeight(15)
                .description("달콤한 꿀사과입니다.")
                .build();

        ItemResponse item1 = itemService.save(itemId1, itemRequest1);
        ItemResponse item2 = itemService.save(itemId2, itemRequest2);
        ItemResponse item3 = itemService.save(itemId3, itemRequest3);

        // WHEN
        itemSearchService = new ItemSearchService(itemRepository);
        List<ItemResponse> fruitItems = itemSearchService.getCategoryItems(Category.FRUIT);
        List<ItemResponse> vegetableItems = itemSearchService.getCategoryItems(Category.VEGETABLE);
        List<ItemResponse> otherItems = itemSearchService.getCategoryItems(Category.OTHER);

        // THEN
        Assertions.assertEquals(2, vegetableItems.size());
        Assertions.assertEquals(1, fruitItems.size());
        Assertions.assertEquals(0, otherItems.size());

        // 카테고리별로 올바른 상품이 조회되었는지 확인
        Assertions.assertEquals(item1.getId(), vegetableItems.get(0).getId());
        Assertions.assertEquals(item2.getId(), vegetableItems.get(1).getId());
        Assertions.assertEquals(item3.getId(), fruitItems.get(0).getId());

    }

}


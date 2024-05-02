package com.gracefullyugly.domain.item.service;

import static com.gracefullyugly.testutil.SetupDataUtils.ADD_CART_ITEM_SUCCESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import com.gracefullyugly.domain.cart.service.CartService;
import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.dto.CartItemResponse;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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
    UserRepository userRepository;

    @Autowired
    CartItemService cartItemService;

    @Autowired
    ItemSearchService itemSearchService;

    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setupUserData() {
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    private Item existingItem;

    // 판매글 여러개 제작할 때 사용
    private ItemRequest createItemRequest(String name, String productionPlace, Category categoryId,
                                          LocalDateTime closedDate,
                                          int minUnitWeight, int price, int totalSalesUnit, int minGroupBuyWeight,
                                          String description) {
        return new ItemRequest(name, productionPlace, categoryId, closedDate, minUnitWeight, price, totalSalesUnit,
                minGroupBuyWeight, description);
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
        Long itemId2 = 2L;
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();

        ItemRequest itemRequest1 = createItemRequest("테스트용 이름1", "테스트용 생산지1", Category.FRUIT,
                LocalDateTime.now().plusDays(5), 3, 7900, 20, 15, "테스트용 내용");
        ItemRequest itemRequest2 = createItemRequest("테스트용 이름2", "테스트용 생산지2", Category.VEGETABLE,
                LocalDateTime.now().plusDays(7), 5, 150000, 50, 30, "테스트용 내용2");

        ItemResponse item1 = itemService.save(itemId1, itemRequest1);
        ItemResponse item2 = itemService.save(itemId2, itemRequest2);

        // 찜 상품에 데이터 추가
        AddCartItemRequest testItemCount1 = AddCartItemRequest.builder().itemCount(2L).build();
        AddCartItemRequest testItemCount2 = AddCartItemRequest.builder().itemCount(5L).build();

        CartItemResponse result1 = cartItemService.addCartItem(testUserId, itemId1, testItemCount1);
        CartItemResponse result2 = cartItemService.addCartItem(testUserId, itemId2, testItemCount2);

        assertThat(result1.getMessage()).isEqualTo(ADD_CART_ITEM_SUCCESS);
        assertThat(result2.getMessage()).isEqualTo(ADD_CART_ITEM_SUCCESS);

        List<CartListResponse> cartList = cartService.getCartList(testUserId);
        assertThat(cartList.size()).isEqualTo(2);

        // WHEN
        List<Item> result = itemRepository.findPopularityItems();

        // THEN
        Assertions.assertEquals(2, result.size()); // 결과는 2개여야 함
        Assertions.assertEquals(item2.getId(), result.get(0).getId()); // 찜 개수가 높은 순으로 정렬되었는지 확인
        Assertions.assertEquals(item1.getId(), result.get(1).getId());

    }

    @Test
    @DisplayName("상품 종류별 검색 목록 조회")
    void getCategoryItemsTest() {
        // GIVEN
        Long itemId1 = 1L;
        Long itemId2 = 2L;

        ItemRequest itemRequest1 = createItemRequest("테스트용 이름1", "테스트용 생산지1", Category.FRUIT,
                LocalDateTime.now().plusDays(5), 3, 7900, 20, 15, "테스트용 내용");
        ItemRequest itemRequest2 = createItemRequest("테스트용 이름2", "테스트용 생산지2", Category.VEGETABLE,
                LocalDateTime.now().plusDays(7), 5, 150000, 50, 30, "테스트용 내용2");

        ItemResponse itemResponse1 = itemService.save(itemId1, itemRequest1);
        ItemResponse itemResponse2 = itemService.save(itemId2, itemRequest2);

        // WHEN
        List<ItemResponse> fruitItems = itemSearchService.getCategoryItems(Category.FRUIT);
        List<ItemResponse> vegetableItems = itemSearchService.getCategoryItems(Category.VEGETABLE);
        List<ItemResponse> otherItems = itemSearchService.getCategoryItems(Category.OTHER);

        // THEN
        Assertions.assertEquals(1, fruitItems.size());
        Assertions.assertEquals(1, vegetableItems.size());
        Assertions.assertEquals(0, otherItems.size());

        // 카테고리별로 올바른 상품이 조회되었는지 확인
        Assertions.assertEquals(itemResponse1.getId(), fruitItems.get(0).getId());
        Assertions.assertEquals(itemResponse2.getId(), vegetableItems.get(0).getId());

    }

}


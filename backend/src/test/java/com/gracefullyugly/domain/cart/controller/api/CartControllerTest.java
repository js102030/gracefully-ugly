package com.gracefullyugly.domain.cart.controller.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import com.gracefullyugly.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CartControllerTest {

    // 테스트용 유저 데이터 Input
    public static final String TEST_LOGIN_ID = "testId";
    public static final String PASSWORD = "testPassword";
    public static final String TEST_NICKNAME = "testNickname";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_ADDRESS = "testAddress";
    public static final Role TEST_ROLE = Role.BUYER;

    // 테스트용 상품 데이터 Input
    public final static String NAME = "테스트용 이름";
    public final static String PRODUCTION_PLACE = "테스트용 생산지";
    public final static Long CATEGORY_ID = 1L;
    public final static LocalDateTime CLOSED_DATE = LocalDateTime.now();
    public final static int MIN_UNIT_WEIGHT = 1000;
    public final static int PRICE = 10000;
    public final static int TOTAL_SALES_UNIT = 10;
    public final static int MIN_GROUP_BUY_WEIGHT = 5000;
    public final static String DESCRIPTION = "테스트용 내용";

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CartController cartController;

    @Autowired
    CartItemService cartItemService;

    @BeforeEach
    void setupUserData() {
        User testUser = new User(
            null,
            SignUpType.GENERAL,
            Role.BUYER,
            TEST_LOGIN_ID,
            passwordEncoder.encode(PASSWORD),
            TEST_NICKNAME,
            TEST_EMAIL,
            TEST_ADDRESS,
            false,
            false,
            false);

        userRepository.save(testUser);
    }

    @BeforeEach
    void setupItemData() {
        ItemRequest testData1 = ItemRequest.builder()
            .name(NAME)
            .productionPlace(PRODUCTION_PLACE)
            .categoryId(CATEGORY_ID)
            .closedDate(CLOSED_DATE)
            .minUnitWeight(MIN_UNIT_WEIGHT)
            .price(PRICE)
            .totalSalesUnit(TOTAL_SALES_UNIT)
            .minGroupBuyWeight(MIN_GROUP_BUY_WEIGHT)
            .description(DESCRIPTION).build();

        ItemRequest testData2 = ItemRequest.builder()
            .name(NAME + 2)
            .productionPlace(PRODUCTION_PLACE + 2)
            .categoryId(CATEGORY_ID)
            .closedDate(CLOSED_DATE)
            .minUnitWeight(MIN_UNIT_WEIGHT + 1000)
            .price(PRICE + 10000)
            .totalSalesUnit(TOTAL_SALES_UNIT + 5)
            .minGroupBuyWeight(MIN_GROUP_BUY_WEIGHT + 5000)
            .description(DESCRIPTION + 2).build();

        itemService.save(1L, testData1);
        itemService.save(1L, testData2);

        cartItemService.addCartItem(userRepository.findByNickname(TEST_NICKNAME).get().getId(), itemRepository.findAll().get(0).getId(), AddCartItemRequest.builder().itemCount(2L).build());
        cartItemService.addCartItem(userRepository.findByNickname(TEST_NICKNAME).get().getId(), itemRepository.findAll().get(1).getId(), AddCartItemRequest.builder().itemCount(5L).build());
    }

    @Test
    @DisplayName("찜 목록 조회 API 테스트")
    void getCartListTest() throws Exception {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();

        // WHEN, THEN
        mockMvc.perform(get("/api/cart/" + testUserId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].itemCount").value(2L))
            .andExpect(jsonPath("$[0].name").value(NAME))
            .andExpect(jsonPath("$[0].price").value(PRICE))
            .andExpect(jsonPath("$[0].categoryId").value(CATEGORY_ID))
            .andExpect(jsonPath("$[1].itemCount").value(5L))
            .andExpect(jsonPath("$[1].name").value(NAME + 2))
            .andExpect(jsonPath("$[1].price").value(PRICE + 10000))
            .andExpect(jsonPath("$[1].categoryId").value(CATEGORY_ID));

    }
}

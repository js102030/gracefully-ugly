package com.gracefullyugly.domain.cartitem.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import com.gracefullyugly.domain.cart.service.CartService;
import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.dto.CartItemResponse;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import com.gracefullyugly.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@Transactional
public class CartItemServiceTest {

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
    public final static Category CATEGORY_ID = Category.VEGETABLE;
    public final static LocalDateTime CLOSED_DATE = LocalDateTime.now();
    public final static int MIN_UNIT_WEIGHT = 1000;
    public final static int PRICE = 10000;
    public final static int TOTAL_SALES_UNIT = 10;
    public final static int MIN_GROUP_BUY_WEIGHT = 5000;
    public final static String DESCRIPTION = "테스트용 내용";

    // Output 메시지
    public final static String ADD_CART_ITEM_SUCCESS = "해당 상품이 찜 목록에 추가되었습니다.";
    public final static String ADD_CART_ITEM_FAIL = "회원 정보가 존재하지 않습니다.";
    public final static String DELETE_CART_ITEM_SUCCESS = "해당 상품이 찜 목록에서 삭제되었습니다.";
    public final static String DELETE_CART_ITEM_NOT_FOUND_CART = "찜 목록이 아직 생성되지 않았습니다.";
    public final static String DELETE_CART_ITEM_NOT_FOUND_ITEM = "해당 상품이 찜 목록에 존재하지 않습니다.";

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

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
    }

    @AfterEach
    void deleteData() {
        itemRepository.deleteAll();
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("찜 목록에 상품 추가 테스트")
    void addCartItemTest() {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        Long testItemId1 = itemRepository.findAll().get(0).getId();
        Long testItemId2 = itemRepository.findAll().get(1).getId();
        AddCartItemRequest testItemCount1 = AddCartItemRequest.builder().itemCount(2L).build();
        AddCartItemRequest testItemCount2 = AddCartItemRequest.builder().itemCount(5L).build();

        // WHEN
        CartItemResponse result1 = cartItemService.addCartItem(testUserId, testItemId1, testItemCount1);
        CartItemResponse result2 = cartItemService.addCartItem(testUserId, testItemId2, testItemCount2);

        // THEN
        assertThat(result1.getMessage()).isEqualTo(ADD_CART_ITEM_SUCCESS);
        assertThat(result2.getMessage()).isEqualTo(ADD_CART_ITEM_SUCCESS);

        List<CartListResponse> cartList = cartService.getCartList(testUserId);
        assertThat(cartList.size()).isEqualTo(2);

        CartListResponse cartListResponse1 = cartList.get(0);
        assertThat(cartListResponse1.getItemCount()).isEqualTo(2L);
        assertThat(cartListResponse1.getName()).isEqualTo(NAME);
        assertThat(cartListResponse1.getPrice()).isEqualTo(PRICE);

        assertThat(cartListResponse1.getCategoryId()).isEqualTo(CATEGORY_ID);

        CartListResponse cartListResponse2 = cartList.get(1);
        assertThat(cartListResponse2.getItemCount()).isEqualTo(5L);
        assertThat(cartListResponse2.getName()).isEqualTo(NAME + 2);
        assertThat(cartListResponse2.getPrice()).isEqualTo(PRICE + 10000);
        assertThat(cartListResponse2.getCategoryId()).isEqualTo(CATEGORY_ID);
    }

    @Test
    @DisplayName("찜 목록에 상품 추가 실패 테스트")
    void addCartItemFailTest() {
        // GIVEN
        Long testUserId = 100L; // 없는 회원
        Long testItemId = itemRepository.findAll().get(0).getId();
        AddCartItemRequest testItemCount = AddCartItemRequest.builder().itemCount(2L).build();

        // WHEN
        CartItemResponse result = cartItemService.addCartItem(testUserId, testItemId, testItemCount);

        // THEN
        assertThat(result.getMessage()).isEqualTo(ADD_CART_ITEM_FAIL);
    }

    @Test
    @DisplayName("찜 목록 상품 삭제 테스트")
    void deleteCartItemTest() {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        Long testItemId = itemRepository.findAll().get(0).getId();
        AddCartItemRequest testItemCount = AddCartItemRequest.builder().itemCount(2L).build();
        cartItemService.addCartItem(testUserId, testItemId, testItemCount);

        // WHEN
        CartItemResponse result = cartItemService.deleteCartItem(testUserId, testItemId);

        // THEN
        assertThat(result.getMessage()).isEqualTo(DELETE_CART_ITEM_SUCCESS);
    }

    @Test
    @DisplayName("찜 목록 상품 삭제 실패 테스트")
    void deleteCartItemFailTest() {
        // GIVEN
        Long testUserId1 = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        cartRepository.createNewCart(testUserId1);
        Long testItemId1 = 100L; // 찜 목록에 없는 상품

        Long testUserId2 = 100L; // 없는 회원
        Long testItemId2 = itemRepository.findAll().get(0).getId();

        // WHEN
        CartItemResponse result1 = cartItemService.deleteCartItem(testUserId1, testItemId1);
        CartItemResponse result2 = cartItemService.deleteCartItem(testUserId2, testItemId2);

        // THEN
        assertThat(result1.getMessage()).isEqualTo(DELETE_CART_ITEM_NOT_FOUND_ITEM);
        assertThat(result2.getMessage()).isEqualTo(DELETE_CART_ITEM_NOT_FOUND_CART);
    }
}

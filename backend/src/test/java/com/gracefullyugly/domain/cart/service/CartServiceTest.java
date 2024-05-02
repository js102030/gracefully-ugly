package com.gracefullyugly.domain.cart.service;

import static com.gracefullyugly.testutil.SetupDataUtils.CATEGORY_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.ITEM_NAME;
import static com.gracefullyugly.testutil.SetupDataUtils.PRICE;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.cart.dto.CartListResponse;
import com.gracefullyugly.domain.cart.repository.CartRepository;
import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import jakarta.transaction.Transactional;
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
public class CartServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemService cartItemService;

    @BeforeEach
    void setupUserData() {
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    @BeforeEach
    void setupItemData() {
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        itemService.save(1L, testItemData.get(0));
        itemService.save(1L, testItemData.get(1));
    }

    @AfterEach
    void deleteData() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("찜 목록 내 상품 조회 메소드 테스트")
    void getCartListTest() {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        Long testItemId1 = itemRepository.findAll().get(0).getId();
        Long testItemId2 = itemRepository.findAll().get(1).getId();
        AddCartItemRequest testItemCount1 = AddCartItemRequest.builder().itemCount(2L).build();
        AddCartItemRequest testItemCount2 = AddCartItemRequest.builder().itemCount(5L).build();

        cartItemService.addCartItem(testUserId, testItemId1, testItemCount1);
        cartItemService.addCartItem(testUserId, testItemId2, testItemCount2);

        // WHEN
        List<CartListResponse> result = cartService.getCartList(testUserId);

        // THEN
        assertThat(result.size()).isEqualTo(2);

        CartListResponse cartListResponse1 = result.get(0);
        assertThat(cartListResponse1.getItemCount()).isEqualTo(2L);
        assertThat(cartListResponse1.getName()).isEqualTo(ITEM_NAME);
        assertThat(cartListResponse1.getPrice()).isEqualTo(PRICE);
        assertThat(cartListResponse1.getCategoryId()).isEqualTo(CATEGORY_ID);

        CartListResponse cartListResponse2 = result.get(1);
        assertThat(cartListResponse2.getItemCount()).isEqualTo(5L);
        assertThat(cartListResponse2.getName()).isEqualTo(ITEM_NAME + 2);
        assertThat(cartListResponse2.getPrice()).isEqualTo(PRICE + 10000);
        assertThat(cartListResponse2.getCategoryId()).isEqualTo(CATEGORY_ID);
    }
}

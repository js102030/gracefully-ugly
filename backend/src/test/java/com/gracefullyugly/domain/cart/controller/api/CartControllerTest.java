package com.gracefullyugly.domain.cart.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.CATEGORY_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.NAME;
import static com.gracefullyugly.testutil.SetupDataUtils.PRICE;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gracefullyugly.domain.cart_item.dto.AddCartItemRequest;
import com.gracefullyugly.domain.cart_item.service.CartItemService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.util.List;
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
public class CartControllerTest {

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
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    @BeforeEach
    void setupItemData() {
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        itemService.save(1L, testItemData.get(0));
        itemService.save(1L, testItemData.get(1));

        cartItemService.addCartItem(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
            itemRepository.findAll().get(0).getId(), AddCartItemRequest.builder().itemCount(2L).build());
        cartItemService.addCartItem(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
            itemRepository.findAll().get(1).getId(), AddCartItemRequest.builder().itemCount(5L).build());
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
            .andExpect(jsonPath("$[0].categoryId").value(CATEGORY_ID.toString()))
            .andExpect(jsonPath("$[1].itemCount").value(5L))
            .andExpect(jsonPath("$[1].name").value(NAME + 2))
            .andExpect(jsonPath("$[1].price").value(PRICE + 10000))
            .andExpect(jsonPath("$[1].categoryId").value(CATEGORY_ID.toString()));

    }
}

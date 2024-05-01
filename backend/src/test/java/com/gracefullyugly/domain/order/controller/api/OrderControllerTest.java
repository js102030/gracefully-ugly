package com.gracefullyugly.domain.order.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.QUANTITY;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADDRESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_PHONE_NUMBER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.order.controller.OrderController;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.order.service.OrderService;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderController orderController;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setupTestData() {
        // 회원 정보 세팅
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));

        // 상품 정보 세팅
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(1));
    }

    @AfterEach
    void deleteTestData() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 정보 조회 API 테스트")
    void getOrderInfTest() throws Exception {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        // WHEN
        ResultActions result = mockMvc.perform(get("/api/orders/" + testUserId + "/" + orderResponse.getOrderId()));

        // THEN
        // TODO: 결제 기능 추가 시 결제 정보도 제대로 받아오는지 이후 테스트 진행해야 함.
        result.andExpect(status().isOk())
            .andExpect(jsonPath("order.createdDate").exists())
            .andExpect(jsonPath("order.lastModifiedDate").exists())
            .andExpect(jsonPath("order.id").exists())
            .andExpect(jsonPath("order.userId").value(testUserId))
            .andExpect(jsonPath("order.address").value(TEST_ADDRESS))
            .andExpect(jsonPath("order.phoneNumber").value(TEST_PHONE_NUMBER))
            .andExpect(jsonPath("nickname").value(TEST_NICKNAME))
            .andExpect(jsonPath("orderItemList[0].name").value(itemList.get(0).getName()))
            .andExpect(jsonPath("orderItemList[0].itemId").value(itemList.get(0).getId()))
            .andExpect(jsonPath("orderItemList[0].quantity").value(QUANTITY))
            .andExpect(jsonPath("orderItemList[1].name").value(itemList.get(1).getName()))
            .andExpect(jsonPath("orderItemList[1].itemId").value(itemList.get(1).getId()))
            .andExpect(jsonPath("orderItemList[1].quantity").value(QUANTITY + 3L));
    }
}

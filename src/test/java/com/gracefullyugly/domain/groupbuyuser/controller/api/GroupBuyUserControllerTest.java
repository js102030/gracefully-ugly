package com.gracefullyugly.domain.groupbuyuser.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.QUANTITY;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.groupbuy.service.GroupBuySearchService;
import com.gracefullyugly.domain.groupbuy.service.GroupBuyService;
import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
import com.gracefullyugly.domain.groupbuyuser.service.GroupBuyUserService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.order.controller.OrderController;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.order.service.OrderService;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testuserdetails.TestUserDetailsService;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupBuyUserControllerTest {

    @Autowired
    GroupBuyUserRepository groupBuyUserRepository;

    @Autowired
    GroupBuyUserService groupBuyUserService;

    @Autowired
    GroupBuyRepository groupBuyRepository;

    @Autowired
    GroupBuyService groupBuyService;

    @Autowired
    GroupBuySearchService groupBuySearchService;

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

    private Long groupBuyId;
    private ItemResponse firstItem;
    private ItemResponse secondItem;
    private Long userId;
    private Long orderId;
    private TestUserDetailsService testUserDetailsService;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setupTestData() {
        // 회원 정보 세팅
        userId = userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder)).getId();
        userRepository.save(
                SetupDataUtils.makeCustomTestUser(null, "customUser", "customUser", "customUser", "custom@custom.com",
                        "customAddress", passwordEncoder));
        userRepository.save(SetupDataUtils.makeTestAdmin(passwordEncoder));

        // 상품 정보 세팅
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        firstItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        secondItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
                testItemData.get(1));

        // 주문 정보 세팅
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
        orderId = orderService.createOrder(userId, testRequest).getOrderId();

        // UserDetails 세팅
        testUserDetailsService = new TestUserDetailsService(userRepository);
        customUserDetails = (CustomUserDetails) testUserDetailsService.loadUserByUsername(TEST_LOGIN_ID);

        // 공동 구매 정보 세팅
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyId = groupBuyRepository.save(
                SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.IN_PROGRESS)).getId();
    }

    @AfterEach
    void deleteTestData() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        groupBuyRepository.deleteAll();
    }

    @Test
    @DisplayName("공동 구매 참여 정보 조회 API 테스트")
    void getGroupBuyUserTest() throws Exception {
        // GIVEN
        List<OrderItem> orderItemList = List.of(new OrderItem(firstItem.getId(), orderId, QUANTITY.intValue()),
                new OrderItem(secondItem.getId(), orderId, QUANTITY.intValue() + 3));
        groupBuyUserService.joinGroupBuy(userId, orderItemList);

        // WHEN
        ResultActions resultFirstItem = mockMvc.perform(get("/api/groupbuyuser/" + firstItem.getId())
                .with(user(customUserDetails)));
        ResultActions resultSecondItem = mockMvc.perform(get("/api/groupbuyuser/" + secondItem.getId())
                .with(user(customUserDetails)));

        // THEN
        resultFirstItem.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].joinDate").isNotEmpty())
                .andExpect(jsonPath("$[0].quantity").value(QUANTITY.intValue()));
        resultSecondItem.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].joinDate").isNotEmpty())
                .andExpect(jsonPath("$[0].quantity").value(QUANTITY.intValue() + 3));
    }

    @Test
    @DisplayName("공동 구매 참여 정보 조회 API 실패 테스트")
    void getGroupBuyUserFailTest() throws Exception {
        // GIVEN
        // 한 건의 공동 구매만 참여
        List<OrderItem> orderItemList = List.of(new OrderItem(firstItem.getId(), orderId, QUANTITY.intValue()));
        groupBuyUserService.joinGroupBuy(userId, orderItemList);

        // WHEN
        ResultActions result = mockMvc.perform(get("/api/groupbuyuser/" + secondItem.getId())
                .with(user(customUserDetails)));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }
}

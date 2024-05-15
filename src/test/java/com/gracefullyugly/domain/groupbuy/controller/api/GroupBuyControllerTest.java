package com.gracefullyugly.domain.groupbuy.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.groupbuy.service.GroupBuySearchService;
import com.gracefullyugly.domain.groupbuy.service.GroupBuyService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.order.controller.OrderController;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class GroupBuyControllerTest {

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

    @BeforeEach
    void setupTestData() {
        // 회원 정보 세팅
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
        userRepository.save(
                SetupDataUtils.makeCustomTestUser(null, "customUser", "customUser", "customUser", "custom@custom.com",
                        "customAddress", passwordEncoder));
        userRepository.save(SetupDataUtils.makeTestAdmin(passwordEncoder));

        // 상품 정보 세팅
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        firstItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(1));

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

//    @Test
//    @DisplayName("공동 구매 단건 조회 API 테스트")
//    void getGroupBuyInfoTest() throws Exception {
//        // WHEN
//        ResultActions result = mockMvc.perform(get("/api/groupbuy/" + groupBuyId));
//
//        // THEN
//        result.andExpect(status().isOk())
//                .andExpect(jsonPath("groupBuyId").value(groupBuyId))
//                .andExpect(jsonPath("itemId").value(firstItem.getId()))
//                .andExpect(jsonPath("itemName").value(firstItem.getName()))
//                .andExpect(jsonPath("groupBuyStatus").value(GroupBuyStatus.IN_PROGRESS.toString()))
//                .andExpect(jsonPath("participantCount").value(0));
//    }

//    @Test
//    @DisplayName("공동 구매 단건 조회 API 실패 테스트")
//    void getGroupBuyInfoFailTest() throws Exception {
//        // GIVEN
//        // 없는 공동 구매 정보
//        Long testFailGroupBuyId = 100L;
//
//        // WHEN
//        ResultActions result = mockMvc.perform(get("/api/groupbuy/" + testFailGroupBuyId));
//
//        // THEN
//        result.andExpect(status().isNotFound());
//    }

    @Test
    @DisplayName("공동 구매 조회 API 테스트")
    void getGroupListByItemIdTest() throws Exception {
        // WHEN
        ResultActions result = mockMvc.perform(get("/api/groupbuy/items/" + firstItem.getId()));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("groupBuyList[0].groupBuyId").value(groupBuyId))
                .andExpect(jsonPath("groupBuyList[0].itemId").value(firstItem.getId()))
                .andExpect(jsonPath("groupBuyList[0].groupBuyStatus").value(GroupBuyStatus.IN_PROGRESS.toString()))
                .andExpect(jsonPath("groupBuyList[0].participantCount").value(0))
                .andExpect(jsonPath("groupBuyList[1].itemId").value(firstItem.getId()))
                .andExpect(jsonPath("groupBuyList[1].groupBuyStatus").value(GroupBuyStatus.CANCELLED.toString()))
                .andExpect(jsonPath("groupBuyList[1].participantCount").value(0));
    }
}

//package com.gracefullyugly.domain.groupbuyuser.service;
//
//import static com.gracefullyugly.testutil.SetupDataUtils.QUANTITY;
//import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.gracefullyugly.common.exception.custom.NotFoundException;
//import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
//import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
//import com.gracefullyugly.domain.groupbuy.service.GroupBuyService;
//import com.gracefullyugly.domain.groupbuyuser.dto.GroupBuyUserFindResponse;
//import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
//import com.gracefullyugly.domain.item.dto.ItemRequest;
//import com.gracefullyugly.domain.item.dto.ItemResponse;
//import com.gracefullyugly.domain.item.repository.ItemRepository;
//import com.gracefullyugly.domain.item.service.ItemService;
//import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
//import com.gracefullyugly.domain.order.repository.OrderRepository;
//import com.gracefullyugly.domain.order.service.OrderService;
//import com.gracefullyugly.domain.orderitem.entity.OrderItem;
//import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
//import com.gracefullyugly.domain.user.repository.UserRepository;
//import com.gracefullyugly.testutil.SetupDataUtils;
//import java.util.List;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class GroupBuyUserServiceTest {
//
//    @Autowired
//    GroupBuyUserRepository groupBuyUserRepository;
//
//    @Autowired
//    GroupBuyUserService groupBuyUserService;
//
//    @Autowired
//    GroupBuyRepository groupBuyRepository;
//
//    @Autowired
//    GroupBuyService groupBuyService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//
//    @Autowired
//    ItemService itemService;
//
//    @Autowired
//    OrderService orderService;
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    OrderItemRepository orderItemRepository;
//
//    private Long groupBuyId;
//
//    private Long userId;
//
//    private Long orderId;
//
//    private ItemResponse firstItem;
//
//    private ItemResponse secondItem;
//
//    @BeforeEach
//    void setupTestData() {
//        // 회원 정보 세팅
//        userId = userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder)).getId();
//        userRepository.save(
//                SetupDataUtils.makeCustomTestUser(null, "customUser", "customUser", "customUser", "custom@custom.com",
//                        "customAddress", passwordEncoder));
//        userRepository.save(SetupDataUtils.makeTestAdmin(passwordEncoder));
//
//        // 상품 정보 세팅
//        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();
//
//        firstItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
//                testItemData.get(0));
//        secondItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
//                testItemData.get(1));
//
//        // 주문 정보 세팅
//        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
//        orderId = orderService.createOrder(userId, testRequest).getOrderId();
//
//        // 공동 구매 정보 세팅
//        groupBuyId = groupBuyRepository.save(
//                SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.IN_PROGRESS)).getId();
//    }
//
//    @AfterEach
//    void deleteTestData() {
//        userRepository.deleteAll();
//        itemRepository.deleteAll();
//        orderRepository.deleteAll();
//        orderItemRepository.deleteAll();
//        groupBuyRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("공동 구매 참여 테스트")
//    void joinGroupBuyTest() {
//        // GIVEN
//        List<OrderItem> orderItemList = List.of(new OrderItem(firstItem.getId(), orderId, QUANTITY.intValue()),
//                new OrderItem(secondItem.getId(), orderId, QUANTITY.intValue() + 3));
//
//        // WHEN
//        List<Long> groupBuyIdList = groupBuyUserService.joinGroupBuy(userId, orderItemList);
//
//        // THEN
//        assertThat(groupBuyIdList.size()).isEqualTo(2);
//        groupBuyIdList.forEach(groupBuyId -> {
//            assertThat(groupBuyRepository.existsById(groupBuyId)).isTrue();
//        });
//    }
//
//    @Test
//    @DisplayName("공동 구매 참여 실패 테스트")
//    void joinGroupBuyFailTest() {
//        // GIVEN
//        // 유효하지 않은 상품이 들어있음
//        List<OrderItem> orderItemList = List.of(new OrderItem(100L, orderId, QUANTITY.intValue()),
//                new OrderItem(secondItem.getId(), orderId, QUANTITY.intValue() + 3));
//
//        // WHEN, THEN
//        Assertions.assertThrows(NotFoundException.class, () -> groupBuyUserService.joinGroupBuy(userId, orderItemList),
//                "유효한 상품이 아닙니다. (item id: " + 100L + ")");
//    }
//
//    @Test
//    @DisplayName("공동 구매 참여 정보 조회 테스트")
//    void getGroupBuyUserTest() {
//        // GIVEN
//        List<OrderItem> orderItemList = List.of(new OrderItem(firstItem.getId(), orderId, QUANTITY.intValue()),
//                new OrderItem(secondItem.getId(), orderId, QUANTITY.intValue() + 3));
//        groupBuyUserService.joinGroupBuy(userId, orderItemList);
//
//        // WHEN
//        List<GroupBuyUserFindResponse> firstGroupInfo = groupBuyUserService.getGroupBuyUser(userId,
//                orderItemList.get(0).getItemId());
//        List<GroupBuyUserFindResponse> secondGroupInfo = groupBuyUserService.getGroupBuyUser(userId,
//                orderItemList.get(1).getItemId());
//
//        // THEN
//        assertThat(firstGroupInfo.get(0).getQuantity()).isEqualTo(QUANTITY.intValue());
//        assertThat(firstGroupInfo.get(0).getJoinDate()).isNotNull();
//        assertThat(secondGroupInfo.get(0).getQuantity()).isEqualTo(QUANTITY.intValue() + 3);
//        assertThat(secondGroupInfo.get(0).getJoinDate()).isNotNull();
//    }
//
//    @Test
//    @DisplayName("공동 구매 참여 정보 조회 실패 테스트")
//    void getGroupBuyUserFailTest() {
//        // GIVEN
//        // 한 건의 공동 구매만 참여
//        List<OrderItem> orderItemList = List.of(new OrderItem(firstItem.getId(), orderId, QUANTITY.intValue()));
//        groupBuyUserService.joinGroupBuy(userId, orderItemList);
//
//        // WHEN
//        List<GroupBuyUserFindResponse> firstResult =  groupBuyUserService.getGroupBuyUser(userId, 100L);
//        List<GroupBuyUserFindResponse> secondResult = groupBuyUserService.getGroupBuyUser(userId, secondItem.getId());
//
//        // THEN
//        assertThat(firstResult.size()).isEqualTo(0);
//        assertThat(secondResult.size()).isEqualTo(0);
//    }
//}

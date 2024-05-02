package com.gracefullyugly.domain.order.service;

import static com.gracefullyugly.testutil.SetupDataUtils.ITEM_NAME;
import static com.gracefullyugly.testutil.SetupDataUtils.NOT_FOUND_ORDER;
import static com.gracefullyugly.testutil.SetupDataUtils.NOT_FOUND_USER;
import static com.gracefullyugly.testutil.SetupDataUtils.ORDER_NO_ITEM;
import static com.gracefullyugly.testutil.SetupDataUtils.QUANTITY;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADDRESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_PHONE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderInfoResponse;
import com.gracefullyugly.domain.order.dto.OrderItemDto;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.orderitem.repository.OrderItemRepository;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

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
    @DisplayName("주문 생성 테스트")
    void createOrderTest() {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());

        // WHEN
        OrderResponse result = orderService.createOrder(testUserId, testRequest);

        // THEN
        assertThat(result.getUserId()).isEqualTo(testUserId);
        assertThat(result.getAddress()).isEqualTo(TEST_ADDRESS);
        assertThat(result.getPhoneNumber()).isEqualTo(TEST_PHONE_NUMBER);
    }

    @Test
    @DisplayName("주문 생성 실패 테스트")
    void createOrderFailTest() {
        // GIVEN
        // 없는 회원 정보
        Long testFailUserId = 100L;
        CreateOrderRequest testNoUserRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());

        // 존재하지 않는 상품 정보만 있음
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<OrderItemDto> testFailOrderItemDtoList = new ArrayList<>();
        testFailOrderItemDtoList.add(new OrderItemDto(121L, 2L));
        testFailOrderItemDtoList.add(new OrderItemDto(111L, 5L));

        CreateOrderRequest testNoItemRequest = CreateOrderRequest.builder()
            .address(TEST_ADDRESS)
            .phoneNumber(TEST_PHONE_NUMBER)
            .itemIdList(testFailOrderItemDtoList)
            .build();

        // WHEN, THEN
        Assertions.assertThrows(NotFoundException.class, () -> orderService.createOrder(testFailUserId, testNoUserRequest), NOT_FOUND_USER);
        Assertions.assertThrows(NotFoundException.class, () -> orderService.createOrder(testUserId, testNoItemRequest), ORDER_NO_ITEM);
    }

    @Test
    @DisplayName("주문 정보 조회 테스트")
    void getOrderInfoTest() {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        // WHEN
        OrderInfoResponse result = orderService.getOrderInfo(testUserId, orderResponse.getOrderId());

        // THEN
        assertThat(result.getOrder().getId()).isEqualTo(orderResponse.getOrderId());
        assertThat(result.getOrder().getUserId()).isEqualTo(testUserId);
        assertThat(result.getOrder().getAddress()).isEqualTo(TEST_ADDRESS);
        assertThat(result.getOrder().getPhoneNumber()).isEqualTo(TEST_PHONE_NUMBER);
        assertThat(result.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(result.getOrderItemList().get(0).getName()).isEqualTo(ITEM_NAME);
        assertThat(result.getOrderItemList().get(0).getPrice()).isEqualTo(itemList.get(0).getPrice());
        assertThat(result.getOrderItemList().get(0).getQuantity()).isEqualTo(QUANTITY);
        assertThat(result.getOrderItemList().get(1).getName()).isEqualTo(ITEM_NAME + 2);
        assertThat(result.getOrderItemList().get(1).getPrice()).isEqualTo(itemList.get(1).getPrice());
        assertThat(result.getOrderItemList().get(1).getQuantity()).isEqualTo(QUANTITY + 3);
    }

    @Test
    @DisplayName("주문 정보 조회 실패 테스트")
    void getOrderInfoFailTest() {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        // 없는 회원 정보
        Long testFailUserId = 100L;

        // 없는 주문 정보
        Long testFailOrderId = 100L;


        // WHEN, THEN
        Assertions.assertThrows(NotFoundException.class, () -> orderService.getOrderInfo(testFailUserId, orderResponse.getOrderId()), NOT_FOUND_USER);
        Assertions.assertThrows(NotFoundException.class, () -> orderService.getOrderInfo(testUserId, testFailOrderId), NOT_FOUND_ORDER);
    }

    @Test
    @DisplayName("주문 정보 주소 변경 테스트")
    void updateOrderAddressTest() {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);
        UpdateOrderAddressRequest request = UpdateOrderAddressRequest.builder().address("NewAddress").build();

        // WHEN
        OrderResponse result = orderService.updateOrderAddress(orderResponse.getOrderId(), request);

        // THEN
        assertThat(result.getOrderId()).isEqualTo(orderResponse.getOrderId());
        assertThat(result.getAddress()).isEqualTo("NewAddress");
    }

    @Test
    @DisplayName("주문 정보 주소 변경 실패 테스트")
    void updateOrderAddressFailTest() {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);
        UpdateOrderAddressRequest request = UpdateOrderAddressRequest.builder().address("NewAddress").build();

        // 없는 주문 정보
        Long testFailOrderId = 100L;

        // WHEN, THEN
        Assertions.assertThrows(NotFoundException.class, () -> orderService.updateOrderAddress(testFailOrderId, request), NOT_FOUND_ORDER);
    }

    @Test
    @DisplayName("주문 정보 연락처 변경 테스트")
    void updateOrderPhoneNumberTest() {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);
        UpdateOrderPhoneNumberRequest request = UpdateOrderPhoneNumberRequest.builder().phoneNumber("01011111111").build();

        // WHEN
        OrderResponse result = orderService.updateOrderPhoneNumber(orderResponse.getOrderId(), request);

        // THEN
        assertThat(result.getOrderId()).isEqualTo(orderResponse.getOrderId());
        assertThat(result.getPhoneNumber()).isEqualTo("01011111111");
    }

    @Test
    @DisplayName("주문 정보 연락처 변경 실패 테스트")
    void updateOrderPhoneNumberFailTest() {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemRepository.findAll());
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);
        UpdateOrderPhoneNumberRequest request = UpdateOrderPhoneNumberRequest.builder().phoneNumber("01011111111").build();

        // 없는 주문 정보
        Long testFailOrderId = 100L;

        // WHEN, THEN
        Assertions.assertThrows(NotFoundException.class, () -> orderService.updateOrderPhoneNumber(testFailOrderId, request), NOT_FOUND_ORDER);
    }
}

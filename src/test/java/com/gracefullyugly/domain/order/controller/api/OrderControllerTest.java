package com.gracefullyugly.domain.order.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.FORBIDDEN;
import static com.gracefullyugly.testutil.SetupDataUtils.NOT_FOUND_ORDER;
import static com.gracefullyugly.testutil.SetupDataUtils.NOT_FOUND_USER;
import static com.gracefullyugly.testutil.SetupDataUtils.ORDER_NO_ITEM;
import static com.gracefullyugly.testutil.SetupDataUtils.QUANTITY;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADDRESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADMIN_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_PHONE_NUMBER;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.order.controller.OrderController;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderItemDto;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.order.dto.UpdateOrderAddressRequest;
import com.gracefullyugly.domain.order.dto.UpdateOrderPhoneNumberRequest;
import com.gracefullyugly.domain.order.repository.OrderRepository;
import com.gracefullyugly.domain.order.service.OrderService;
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

    private TestUserDetailsService testUserDetailsService;
    private CustomUserDetails customUserDetails;

    // 다른 회원 정보
    private CustomUserDetails customAnotherUserDetails;

    // 없는 회원 정보
    private CustomUserDetails notExist;

    // 어드민 정보
    private CustomUserDetails admin;

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

        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(1));

        // UserDetails 세팅
        testUserDetailsService = new TestUserDetailsService(userRepository);
        customUserDetails = (CustomUserDetails) testUserDetailsService.loadUserByUsername(TEST_LOGIN_ID);
        customAnotherUserDetails = (CustomUserDetails) testUserDetailsService.loadUserByUsername("customUser");

        // 어드민 UserDetails 세팅
        admin = (CustomUserDetails) testUserDetailsService.loadUserByUsername(TEST_ADMIN_LOGIN_ID);

        // 없는 회원 UserDetails 세팅
        notExist = new CustomUserDetails(
                SetupDataUtils.makeCustomTestUser(100L, "NotExist", "NotExist", "NotExist", "NotExist", "NotExist",
                        passwordEncoder));
    }

    @AfterEach
    void deleteTestData() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 생성 API 테스트")
    void createOrderTest() throws Exception {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);

        // WHEN
        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(testRequest)));

        // THEN
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("orderId").exists())
                .andExpect(jsonPath("userId").value(testUserId))
                .andExpect(jsonPath("address").value(TEST_ADDRESS))
                .andExpect(jsonPath("phoneNumber").value(TEST_PHONE_NUMBER));
    }

    @Test
    @DisplayName("주문 생성 API 실패 테스트")
    void createOrderFailTest() throws Exception {
        // GIVEN
        // 없는 회원 정보
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);

        // 없는 상품 정보들만 주문을 시도
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        CreateOrderRequest testNoItemRequest = CreateOrderRequest.builder()
                .address(TEST_ADDRESS)
                .phoneNumber(TEST_PHONE_NUMBER)
                .itemIdList(List.of(OrderItemDto.builder().itemId(111L).quantity(1L).build(),
                        OrderItemDto.builder().itemId(131L).quantity(12L).build())).build();

        // 주소가 공란
        CreateOrderRequest testInvalidAddressRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        testInvalidAddressRequest.setAddress("");

        // 유효하지 않은 연락처
        CreateOrderRequest testInvalidPhoneNumberRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        testInvalidPhoneNumberRequest.setPhoneNumber("11111111111111111");

        // WHEN
        ResultActions resultNoUser = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(notExist))
                .content(objectMapper.writeValueAsString(testRequest)));
        ResultActions resultNoItem = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(testNoItemRequest)));
        ResultActions resultNoAddress = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(testInvalidAddressRequest)));
        ResultActions resultInvalidPhoneNumber = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(testInvalidPhoneNumberRequest)));

        // THEN
        resultNoUser.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(NOT_FOUND_USER));
        resultNoItem.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(ORDER_NO_ITEM));
        resultNoAddress.andExpect(status().isBadRequest());
        resultInvalidPhoneNumber.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 정보 조회 API 테스트")
    void getOrderInfoTest() throws Exception {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        // WHEN
        ResultActions result = mockMvc.perform(
                get("/api/orders/" + orderResponse.getOrderId()).with(user(customUserDetails)));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("order.createdDate").exists())
                .andExpect(jsonPath("order.lastModifiedDate").exists())
                .andExpect(jsonPath("order.id").exists())
                .andExpect(jsonPath("order.userId").value(testUserId))
                .andExpect(jsonPath("order.address").value(TEST_ADDRESS))
                .andExpect(jsonPath("order.phoneNumber").value(TEST_PHONE_NUMBER))
                .andExpect(jsonPath("nickname").value(TEST_NICKNAME))
                .andExpect(jsonPath("orderItemList[0].itemName").value(itemList.get(0).getName()))
                .andExpect(jsonPath("orderItemList[0].itemId").value(itemList.get(0).getId()))
                .andExpect(jsonPath("orderItemList[0].itemPrice").value(itemList.get(0).getPrice()))
                .andExpect(jsonPath("orderItemList[0].quantity").value(QUANTITY))
                .andExpect(jsonPath("orderItemList[1].itemName").value(itemList.get(1).getName()))
                .andExpect(jsonPath("orderItemList[1].itemId").value(itemList.get(1).getId()))
                .andExpect(jsonPath("orderItemList[1].quantity").value(QUANTITY + 3L))
                .andExpect(jsonPath("orderItemList[1].itemPrice").value(itemList.get(1).getPrice()));
    }

    @Test
    @DisplayName("주문 정보 조회 API 실패 테스트")
    void getOrderInfoFailTest() throws Exception {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        // 없는 주문 정보
        Long testFailOrderId = 100L;

        // WHEN
        ResultActions resultNoUser = mockMvc.perform(
                get("/api/orders/" + orderResponse.getOrderId()).with(user(notExist)));
        ResultActions resultNoOrder = mockMvc.perform(
                get("/api/orders/" + testFailOrderId).with(user(customUserDetails)));

        // THEN
        resultNoUser.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(NOT_FOUND_USER));
        resultNoOrder.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(NOT_FOUND_ORDER));
    }

    @Test
    @DisplayName("주문 정보 주소 수정 API 테스트")
    void updateOrderAddressTest() throws Exception {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        UpdateOrderAddressRequest request = UpdateOrderAddressRequest.builder().address("NewAddress").build();
        UpdateOrderAddressRequest requestAdmin = UpdateOrderAddressRequest.builder().address("AdminAddress").build();

        // WHEN
        ResultActions result = mockMvc.perform(put("/api/orders/address/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(request)));
        ResultActions resultAdmin = mockMvc.perform(put("/api/orders/address/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(admin))
                .content(objectMapper.writeValueAsString(requestAdmin)));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("orderId").value(orderResponse.getOrderId()))
                .andExpect(jsonPath("address").value("NewAddress"));
        resultAdmin.andExpect(status().isOk())
                .andExpect(jsonPath("orderId").value(orderResponse.getOrderId()))
                .andExpect(jsonPath("address").value("AdminAddress"));
    }

    @Test
    @DisplayName("주문 정보 주소 수정 API 실패 테스트")
    void updateOrderAddressFailTest() throws Exception {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        UpdateOrderAddressRequest request = UpdateOrderAddressRequest.builder().address("NewAddress").build();

        // 없는 주문 정보
        Long testFailOrderId = 100L;

        // 주소가 공란
        UpdateOrderAddressRequest requestFail = UpdateOrderAddressRequest.builder().address("").build();

        // WHEN
        ResultActions resultForbiddenUser = mockMvc.perform(put("/api/orders/address/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customAnotherUserDetails))
                .content(objectMapper.writeValueAsString(request)));
        ResultActions resultNoOrder = mockMvc.perform(put("/api/orders/address/" + testFailOrderId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(request)));
        ResultActions resultNoAddress = mockMvc.perform(put("/api/orders/address/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(requestFail)));

        // THEN
        resultForbiddenUser.andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value(FORBIDDEN));
        resultNoOrder.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(NOT_FOUND_ORDER));
        resultNoAddress.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("주문 정보 연락처 수정 API 테스트")
    void updateOrderPhoneNumberTest() throws Exception {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        UpdateOrderPhoneNumberRequest request = UpdateOrderPhoneNumberRequest.builder().phoneNumber("01055559999")
                .build();
        UpdateOrderPhoneNumberRequest requestAdmin = UpdateOrderPhoneNumberRequest.builder().phoneNumber("01012345678")
                .build();

        // WHEN
        ResultActions result = mockMvc.perform(put("/api/orders/phone_number/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(request)));
        ResultActions resultAdmin = mockMvc.perform(put("/api/orders/phone_number/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(admin))
                .content(objectMapper.writeValueAsString(requestAdmin)));

        // THEN
        result.andExpect(status().isOk())
                .andExpect(jsonPath("orderId").value(orderResponse.getOrderId()))
                .andExpect(jsonPath("phoneNumber").value("01055559999"));
        resultAdmin.andExpect(status().isOk())
                .andExpect(jsonPath("orderId").value(orderResponse.getOrderId()))
                .andExpect(jsonPath("phoneNumber").value("01012345678"));
    }

    @Test
    @DisplayName("주문 정보 연락처 수정 API 실패 테스트")
    void updateOrderPhoneNumberFailTest() throws Exception {
        // GIVEN
        // 기본 주문 정보 세팅
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> itemList = itemRepository.findAll();
        CreateOrderRequest testRequest = SetupDataUtils.makeCreateOrderRequest(itemList);
        OrderResponse orderResponse = orderService.createOrder(testUserId, testRequest);

        UpdateOrderPhoneNumberRequest request = UpdateOrderPhoneNumberRequest.builder().phoneNumber("01055559999")
                .build();

        // 없는 주문 정보
        Long testFailOrderId = 100L;

        // 유효하지 않은 연락처
        UpdateOrderPhoneNumberRequest requestFail = UpdateOrderPhoneNumberRequest.builder().phoneNumber("-1").build();

        // WHEN
        ResultActions resultForbiddenUser = mockMvc.perform(put("/api/orders/phone_number/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customAnotherUserDetails))
                .content(objectMapper.writeValueAsString(request)));
        ResultActions resultNoOrder = mockMvc.perform(put("/api/orders/phone_number/" + testFailOrderId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(request)));
        ResultActions resultNoAddress = mockMvc.perform(put("/api/orders/phone_number/" + orderResponse.getOrderId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(customUserDetails))
                .content(objectMapper.writeValueAsString(requestFail)));

        // THEN
        resultForbiddenUser.andExpect(status().isForbidden())
                .andExpect(jsonPath("$").value(FORBIDDEN));
        resultNoOrder.andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value(NOT_FOUND_ORDER));
        resultNoAddress.andExpect(status().isBadRequest());
    }
}

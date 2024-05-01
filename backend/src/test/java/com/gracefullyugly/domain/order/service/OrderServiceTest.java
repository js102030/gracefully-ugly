package com.gracefullyugly.domain.order.service;

import static com.gracefullyugly.testutil.SetupDataUtils.CREATE_ORDER_SUCCESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADDRESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_PHONE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.order.dto.CreateOrderRequest;
import com.gracefullyugly.domain.order.dto.OrderItemDto;
import com.gracefullyugly.domain.order.dto.OrderResponse;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    @DisplayName("주문 생성 테스트")
    void createOrderTest() {
        // GIVEN
        Long testUserId = userRepository.findByNickname(TEST_NICKNAME).get().getId();
        List<Item> testItemList = itemRepository.findAll();
        List<OrderItemDto> testOrderItemDtoList = new ArrayList<>();
        testOrderItemDtoList.add(new OrderItemDto(testItemList.get(0).getId(), 2L));
        testOrderItemDtoList.add(new OrderItemDto(testItemList.get(1).getId(), 5L));

        CreateOrderRequest testRequest = CreateOrderRequest.builder()
            .address(TEST_ADDRESS)
            .phoneNumber(TEST_PHONE_NUMBER)
            .itemIdList(testOrderItemDtoList)
            .build();

        // WHEN
        OrderResponse result = orderService.createOrder(testUserId, testRequest);

        // THEN
        assertThat(result.getMessage()).isEqualTo(CREATE_ORDER_SUCCESS);
    }
}

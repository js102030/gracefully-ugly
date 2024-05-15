package com.gracefullyugly.domain.groupbuy.service;

import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GroupBuyServiceTest {

    @Autowired
    GroupBuyRepository groupBuyRepository;

    @Autowired
    GroupBuyService groupBuyService;

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

    private Long groupBuyId;

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

        ItemResponse firstItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
                testItemData.get(0));
        ItemResponse secondItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
                testItemData.get(1));

        // 공동 구매 정보 세팅
        groupBuyId = groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.IN_PROGRESS)).getId();
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
    @DisplayName("공동 구매 상태 변경 테스트")
    void updateGroupStatusTest() {
        // GIVEN
        GroupBuyStatus changeStatus = GroupBuyStatus.COMPLETED;

        // WHEN
        boolean result = groupBuyService.updateGroupStatusByGroupBuyId(groupBuyId, changeStatus);

        // THEN
        assertThat(result).isTrue();
        assertThat(groupBuyRepository.findById(groupBuyId).get().getGroupBuyStatus()).isEqualTo(
                GroupBuyStatus.COMPLETED);
    }

    @Test
    @DisplayName("공동 구매 상태 변경 실패 테스트")
    void updateGroupStatusFailTest() {
        // GIVEN
        // 없는 공동 구매 정보
        Long testFailGroupBuyId = 100L;
        GroupBuyStatus changeStatus = GroupBuyStatus.COMPLETED;

        // WHEN
        boolean result = groupBuyService.updateGroupStatusByGroupBuyId(testFailGroupBuyId, changeStatus);

        // THEN
        assertThat(result).isFalse();
    }
}

package com.gracefullyugly.domain.groupbuy.service;

import static com.gracefullyugly.testutil.SetupDataUtils.CLOSED_DATE;
import static com.gracefullyugly.testutil.SetupDataUtils.NOT_FOUND_GROUP_BUY;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyListResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuySelectDto;
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
import java.time.format.DateTimeFormatter;
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
public class GroupBuySearchServiceTest {

    @Autowired
    GroupBuyRepository groupBuyRepository;

    @Autowired
    GroupBuyService groupBuyService;

    @Autowired
    GroupBuySearchService groupBuySearchService;

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

        firstItem = itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
                testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(),
                testItemData.get(1));

        // 공동 구매 정보 세팅
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
        groupBuyRepository.save(SetupDataUtils.createGroupBuy(firstItem.getId(), GroupBuyStatus.CANCELLED));
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
    @DisplayName("공동 구매 단건 조회 테스트")
    void getGroupBuyInfoTest() {
        // WHEN
        GroupBuyInfoResponse result = groupBuySearchService.getGroupBuyInfo(groupBuyId);

        // THEN
        assertThat(result.getGroupBuyId()).isEqualTo(groupBuyId);
        assertThat(result.getItemId()).isEqualTo(firstItem.getId());
        assertThat(result.getGroupBuyStatus()).isEqualTo(GroupBuyStatus.IN_PROGRESS);
        assertThat(result.getItemName()).isEqualTo(firstItem.getName());
        assertThat(result.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).isEqualTo(
                CLOSED_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(result.getParticipantCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("공동 구매 단건 조회 실패 테스트")
    void getGroupBuyInfoFailTest() {
        // GIVEN
        // 없는 공동 구매 정보
        Long testFailGroupBuyId = 100L;

        // WHEN, THEN
        Assertions.assertThrows(NotFoundException.class,
                () -> groupBuySearchService.getGroupBuyInfo(testFailGroupBuyId), NOT_FOUND_GROUP_BUY);
    }

    @Test
    @DisplayName("공동 구매 조회 테스트")
    void getGroupBuyListByItemIdTest() {
        // WHEN
        GroupBuyListResponse result = groupBuySearchService.getGroupBuyListByItemId(firstItem.getId());
        List<GroupBuySelectDto> resultList = result.getGroupBuyList();

        // THEN
        assertThat(resultList.size()).isEqualTo(5);

        assertThat(resultList.get(0).getGroupBuyId()).isEqualTo(groupBuyId);
        assertThat(resultList.get(0).getItemId()).isEqualTo(firstItem.getId());
        assertThat(resultList.get(0).getGroupBuyStatus()).isEqualTo(GroupBuyStatus.IN_PROGRESS);
        assertThat(resultList.get(0).getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).isEqualTo(
                CLOSED_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(resultList.get(0).getParticipantCount()).isEqualTo(0);

        assertThat(resultList.get(1).getItemId()).isEqualTo(firstItem.getId());
        assertThat(resultList.get(1).getGroupBuyStatus()).isEqualTo(GroupBuyStatus.CANCELLED);
        assertThat(resultList.get(1).getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).isEqualTo(
                CLOSED_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(resultList.get(1).getParticipantCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("공동 구매 조회 실패 테스트")
    void getGroupBuyListByItemIdFailTest() {
        // GIVEN
        // 없는 상품 정보
        Long testFailItemId = 100L;

        // WHEN
        GroupBuyListResponse result = groupBuySearchService.getGroupBuyListByItemId(testFailItemId);
        List<GroupBuySelectDto> resultList = result.getGroupBuyList();

        // THEN
        assertThat(resultList.size()).isEqualTo(0);
    }
}

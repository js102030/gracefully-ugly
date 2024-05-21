package com.gracefullyugly.domain.groupbuyuser.service;

import static com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.COMPLETED;
import static com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus.IN_PROGRESS;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.groupbuy.service.GroupBuySearchService;
import com.gracefullyugly.domain.groupbuyuser.dto.GroupBuyUserFindResponse;
import com.gracefullyugly.domain.groupbuyuser.entity.GroupBuyUser;
import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class GroupBuyUserService {

    private final ItemSearchService itemSearchService;
    private final GroupBuyRepository groupBuyRepository;
    private final GroupBuyUserRepository groupBuyUserRepository;
    private final GroupBuyUserSearchService groupBuyUserSearchService;
    private final GroupBuySearchService groupBuySearchService;

    public List<Long> joinGroupBuy(Long userId, List<OrderItem> orderItemList) {
        List<Long> groupBuyIds = new ArrayList<>();

        orderItemList.forEach(orderItem -> {
            Long itemId = orderItem.getItemId();
            GroupBuy findGroupBuy = getOrCreateGroupBuyByItemId(itemId);

            groupBuyUserRepository.save(
                    new GroupBuyUser(
                            findGroupBuy.getId(),
                            userId,
                            orderItem.getOrderId(),
                            LocalDateTime.now(),
                            orderItem.getQuantity())
            );

            Item findItem = itemSearchService.findValidItemById(itemId);
            List<GroupBuyUser> findGroupBuyUser = groupBuyUserSearchService.findGroupBuyUsersByItemIdAndGroupBuyId(
                    itemId,
                    findGroupBuy.getId());

            int completeQuantity = findItem.getMinGroupBuyWeight() / findItem.getMinUnitWeight();
            int totalQuantity = 0;
            for (GroupBuyUser groupBuyUser : findGroupBuyUser) {
                totalQuantity += groupBuyUser.getQuantity();
            }

            if (totalQuantity >= completeQuantity) {
                groupBuySearchService.findInProgressGroupBuyByItemId(findItem.getId())
                        .orElseThrow(
                                () -> new NotFoundException("itemId : " + findItem.getId() + "의 공동구매 정보가 존재하지 않습니다."))
                        .updateGroupBuyStatus(COMPLETED);
            }

            groupBuyIds.add(findGroupBuy.getId());

        });

        return groupBuyIds;
    }

    public List<GroupBuyUserFindResponse> getGroupBuyUser(Long userId, Long itemId) {
        List<GroupBuyUser> groupBuyUserList = groupBuyUserRepository.findByUserIdAndItemId(userId, itemId);

        return groupBuyUserList.stream()
                .map(groupBuyUser ->
                        GroupBuyUserFindResponse.builder()
                                .joinDate(groupBuyUser.getJoinDate())
                                .quantity(groupBuyUser.getQuantity())
                                .build())
                .toList();
    }

    private GroupBuy getOrCreateGroupBuyByItemId(Long itemId) {
        Optional<GroupBuy> optionalGroupBuy = groupBuySearchService.findInProgressGroupBuyByItemId(itemId);

        return optionalGroupBuy.orElseGet(
                () -> groupBuyRepository.save(new GroupBuy(itemId, IN_PROGRESS)));
    }
}

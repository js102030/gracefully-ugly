package com.gracefullyugly.domain.groupbuy.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyUpdateRequest;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyUpdateResponse;
import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.groupbuyuser.entity.GroupBuyUser;
import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.orderitem.entity.OrderItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class GroupBuyService {

    private ItemRepository itemRepository;
    private GroupBuyRepository groupBuyRepository;
    private GroupBuyUserRepository groupBuyUserRepository;

    public boolean updateGroupStatus(Long groupBuyId, GroupBuyStatus status) {
        Optional<GroupBuy> groupBuy = groupBuyRepository.findById(groupBuyId);

        if (groupBuy.isEmpty()) {
            return false;
        }
        groupBuy.get().updateGroupBuyStatus(status);

        return true;
    }

    public void joinGroupBuy(Long userId, List<OrderItem> orderItemList) {
        orderItemList.forEach(orderItem -> {
            Long groupId = getGroupBuyId(orderItem.getItemId());

            groupBuyUserRepository.save(new GroupBuyUser(groupId, userId, LocalDateTime.now(), orderItem.getQuantity()));
        });
    }

    /**
     * 해당 상품의 현재 진행중인 공동 구매를 조사해 그 공동 구매의 ID를 반환하는 메소드 입니다.
     * 만약, 진행중인 공동 구매가 없다면 공동 구매를 생성한 뒤 그 ID를 반환합니다.
     */
    private Long getGroupBuyId(Long itemId) {
        Optional<GroupBuy> groupBuyOptional = groupBuyRepository.findProgressGroupBuyByItemId(itemId);

        if (groupBuyOptional.isEmpty()) {
            Item result = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(" 상품 정보가 없습니다. (item id: " + itemId + ")"));

            return groupBuyRepository.save(new GroupBuy(itemId, GroupBuyStatus.IN_PROGRESS, result.getClosedDate())).getId();
        }
        else {
            return groupBuyOptional.get().getId();
        }
    }
}

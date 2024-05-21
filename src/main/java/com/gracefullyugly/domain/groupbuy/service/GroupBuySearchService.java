package com.gracefullyugly.domain.groupbuy.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyListResponse;
import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class GroupBuySearchService {

    private GroupBuyRepository groupBuyRepository;

    public GroupBuyInfoResponse getGroupBuyInfo(Long groupBuyId) {
        return groupBuyRepository.findGroupBuyById(groupBuyId)
                .orElseThrow(() -> new NotFoundException("공동 구매 정보가 없습니다."));
    }

    public GroupBuyListResponse getGroupBuyListByItemId(Long itemId) {
        return new GroupBuyListResponse(groupBuyRepository.findTop5ByItemIdOrderByIdDesc(itemId));
    }

    public Optional<GroupBuy> findInProgressGroupBuyByItemId(Long itemId) {
        return groupBuyRepository.findByItemIdAndGroupBuyStatus(itemId, GroupBuyStatus.IN_PROGRESS);
    }
}

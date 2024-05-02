package com.gracefullyugly.domain.groupbuy.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyListResponse;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
}

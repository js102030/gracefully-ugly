package com.gracefullyugly.domain.groupbuy.service;

import com.gracefullyugly.domain.groupbuy.entity.GroupBuy;
import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class GroupBuyService {

    private GroupBuyRepository groupBuyRepository;

    public boolean updateGroupStatus(Long groupBuyId, GroupBuyStatus status) {
        Optional<GroupBuy> groupBuy = groupBuyRepository.findById(groupBuyId);

        if (groupBuy.isEmpty()) {
            return false;
        }
        groupBuy.get().updateGroupBuyStatus(status);

        return true;
    }
}

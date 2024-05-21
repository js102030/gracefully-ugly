package com.gracefullyugly.domain.groupbuyuser.service;

import com.gracefullyugly.domain.groupbuyuser.entity.GroupBuyUser;
import com.gracefullyugly.domain.groupbuyuser.repository.GroupBuyUserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupBuyUserSearchService {

    private final GroupBuyUserRepository groupBuyUserRepository;

    public List<GroupBuyUser> findGroupBuyUsersByItemIdAndGroupBuyId(Long itemId, Long groupBuyId) {
        List<GroupBuyUser> findGroupBuyUser = groupBuyUserRepository.findGroupBuyUsersByItemIdAndGroupBuyId(itemId,
                groupBuyId);

        if (findGroupBuyUser.isEmpty()) {
            throw new IllegalArgumentException("해당 상품의 구매자가 없습니다.");
        }

        return findGroupBuyUser;
    }
}

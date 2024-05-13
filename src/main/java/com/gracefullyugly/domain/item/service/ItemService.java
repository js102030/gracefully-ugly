package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.groupbuy.service.GroupBuyService;
import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionRequest;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final GroupBuyService groupBuyService;
    private final ItemSearchService itemSearchService;
    private final ItemRepository itemRepository;

    public ItemResponse save(Long userId, ItemRequest request) {
        Item itemEntity = request.toEntity(userId);

        Item savedItem = itemRepository.save(itemEntity);

        return ItemDtoUtil.itemToItemResponse(savedItem);
    }

    public ItemResponse updateDescription(Long itemId, Long userId, UpdateDescriptionRequest request) {
        Item findItem = itemSearchService.findById(itemId);

        if (!findItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        findItem.updateDescription(request.getDescription());

        return ItemDtoUtil.itemToItemResponse(findItem);
    }

    public void deletedById(Long itemId, Long userId) {
        Item findItem = itemSearchService.findById(itemId);

        if (!findItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 삭제 가능합니다.");
        }

        findItem.delete();
        groupBuyService.updateGroupBuyStatusToCancelByItemId(itemId);
    }

}

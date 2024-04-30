package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionDto;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemSearchService itemSearchService;
    private final ItemRepository itemRepository;

    public ItemResponse save(Long userId, ItemRequest request) {
        Item itemEntity = request.toEntity(userId);

        Item savedItem = itemRepository.save(itemEntity);

        return ItemDtoUtil.itemToItemResponse(savedItem);
    }


    public UpdateAddressDto updateDescription(Long itemId, UpdateDescriptionDto updateDescriptionDto) {
        Item findItem = itemSearchService.findById(itemId);

        return findItem.updateDescription(updateDescriptionDto.getDescription());
    }

    public void deletedById(Long itemId) {
        Item findItem = itemSearchService.findById(itemId);

        findItem.delete();
    }


}

package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionDto;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
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


    public UpdateDescriptionDto updateDescription(Long itemId, Long userId,UpdateDescriptionDto updateDescriptionDto) {
        Item findItem = itemSearchService.findById(itemId);
        System.out.println("글작성 본인 아이디 : "+findItem.getUserId());
        System.out.println("비교하려는 아이디 : "+userId);
        if (findItem == null) {
            throw new IllegalArgumentException("해당 글을 찾을 수 없습니다.");
        }
        if (!findItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 글은 작성자만 수정 가능합니다.");
        }
        return findItem.updateDescription(updateDescriptionDto.getDescription());
    }

    public void deletedById(Long itemId, Long userId) {
        Item findItem = itemSearchService.findById(itemId);
        if (findItem == null) {
            throw new IllegalArgumentException("해당 글을 찾을 수 없습니다.");
        }
        if (!findItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 글은 작성자만 삭제 가능합니다.");
        }
        findItem.delete();
    }

}

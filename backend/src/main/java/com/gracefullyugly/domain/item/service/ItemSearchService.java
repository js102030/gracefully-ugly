package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemSearchService {

    private final ItemRepository itemRepository;

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException(itemId + "에 해당하는 상품이 없습니다."));
    }

    public ItemResponse findOneItem(Long itemId) {
        Item findItem = findById(itemId);

        return ItemDtoUtil.itemToItemResponse(findItem);
    }

    // TODO : 페이징 처리 후 리팩토링 예정
    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }
}

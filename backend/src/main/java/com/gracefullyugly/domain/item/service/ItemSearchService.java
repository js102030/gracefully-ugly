package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    // 72시간 이내 마감임박 상품 목록 조회
    public List<ItemResponse> getImpendingItems() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = now.plusHours(72);

        List<Item> impendingItems = itemRepository.findRandomImpendingItems(endTime);

        return impendingItems.stream()
                .map(ItemDtoUtil::itemToItemResponse)
                .collect(Collectors.toList());
    }

    // 인기 상품 목록 조회 / 찜 개수 기준
    public List<ItemResponse> findMostAddedToCartItems() {
        List<Item> popularityItems = itemRepository.findMostAddedToCartItems();

        if (popularityItems.isEmpty()) {
            throw new IllegalArgumentException("인기 상품이 없습니다.");
        }

        return popularityItems.stream()
                .map(ItemDtoUtil::itemToItemResponse)
                .collect(Collectors.toList());
    }

    // 상품 종류별 검색 목록 조회
    public List<ItemResponse> getCategoryItems(Category categoryId) {
        List<Item> categoryItems = itemRepository.findCategoryItems(categoryId);

        return categoryItems.stream()
                .map(ItemDtoUtil::itemToItemResponse)
                .collect(Collectors.toList());
    }

}

package com.gracefullyugly.domain.item.service;

import com.gracefullyugly.domain.item.dto.ItemWithImageUrlResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemSearchService {

    private final ItemRepository itemRepository;

    public Item findById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException(itemId + "에 해당하는 상품이 없습니다."));
    }

    public ItemWithImageUrlResponse findOneItem(Long itemId) {
        return itemRepository.findOneItemWithImage(itemId);
    }

    public List<ItemWithImageUrlResponse> findAllItems() {
        return itemRepository.findAllItemsWithImages();
    }

    // 72시간 이내 마감임박 상품 목록 조회
    public List<ItemWithImageUrlResponse> getImpendingItems() {
        LocalDateTime endTime = LocalDateTime.now().plusHours(72);

        return itemRepository.findRandomImpendingItems(endTime);
    }

    // 인기 상품 목록 조회 / 찜 개수 기준
    public List<ItemWithImageUrlResponse> findMostAddedToCartItems() {
        List<ItemWithImageUrlResponse> response = itemRepository.findMostAddedToCartItems();

        return response;
    }

    // 상품 종류별 검색 목록 조회
    public List<ItemWithImageUrlResponse> getCategoryItems(String categoryId) {
        return itemRepository.findCategoryItemsWithImageUrl(
                categoryId);
    }

}

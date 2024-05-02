package com.gracefullyugly.domain.item.controller.api;

import com.gracefullyugly.domain.item.dto.ItemDtoUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionDto;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;

import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;
    private final ItemSearchService itemSearchService;

    // 판매글 생성
    @PostMapping("/items")
    public ResponseEntity<ItemResponse> addItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                @RequestBody ItemRequest request) {
        final ItemResponse savedResponse = itemService.save(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedResponse);
    }

    // 판매글 목록 조회
    // TODO : 페이징 처리 후 리팩토링 예정
    @GetMapping("/items")
    public ResponseEntity<List<ItemResponse>> showItems() {
        // TODO : List를 반환하지 않고 ApiResponse를 반환하는 것이 좋아 보임
        List<Item> itemList = itemSearchService.findAllItems();
        List<ItemResponse> responseList = itemList.stream()
                .map(ItemDtoUtil::itemToItemResponse)
                .toList();
        return ResponseEntity.ok(responseList);
    }


    // 판매글 상세 조회
    @GetMapping("/items/{itemId}")
    public ResponseEntity<ItemResponse> showOneItem(@PathVariable Long itemId) {
        final ItemResponse itemResponse = itemSearchService.findOneItem(itemId);

        return ResponseEntity
                .ok(itemResponse);

    }

    // 판매글 수정
    @PutMapping("/items/{itemId}")
    public ResponseEntity<UpdateDescriptionDto> updateDescription(@PathVariable Long itemId,
                                                                  @AuthenticationPrincipal Long userId,
                                                                  @Valid @RequestBody UpdateDescriptionDto updateDescriptionDto) {
        final UpdateDescriptionDto updateAddress = itemService.updateDescription(itemId, userId, updateDescriptionDto);

        return ResponseEntity
                .ok(updateAddress);
    }

    // 판매글 삭제
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteOneItem(@PathVariable Long itemId,
                                              @AuthenticationPrincipal Long userId) {
        itemService.deletedById(itemId, userId);

        return ResponseEntity.
                noContent()
                .build();
    }

    // 72시간 이내 마감임박 상품 목록 조회
    @GetMapping("/items/impending")
    public ResponseEntity<List<?>> showImpendingItems() {
        List<ItemResponse> itemResponseList = itemSearchService.getImpendingItems();
        return ResponseEntity
                .ok(itemResponseList);

    }
    // 인기 상품 목록 조회
    @GetMapping("/items/popularity")
    public ResponseEntity<List<?>> showPopularity() {
        List<ItemResponse> itemResponseList = itemSearchService.getPopularityItems();
        return ResponseEntity
                .ok(itemResponseList);
    }


    // 상품 종류별 검색 목록 조회
    @GetMapping("/items/category/{categoryId}")
    public ResponseEntity<List<?>> showCategory(@PathVariable Category categoryId) {
        List<ItemResponse> itemResponseList = itemSearchService.getCategoryItems(categoryId);
        return ResponseEntity
                .ok(itemResponseList);
    }

}

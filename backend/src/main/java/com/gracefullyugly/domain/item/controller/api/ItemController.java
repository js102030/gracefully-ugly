package com.gracefullyugly.domain.item.controller.api;

import com.gracefullyugly.domain.image.service.ImageUploadService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.ItemWithImageUrlResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionRequest;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;
    private final ItemSearchService itemSearchService;
    private final ImageUploadService imageUploadService;

    // 판매글 생성
    @PostMapping("/items")
    public ResponseEntity<ItemResponse> addItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                @ModelAttribute ItemRequest request,
                                                @RequestParam("productImage") MultipartFile file) {

        ItemResponse response = itemService.save(userId, request);

        if (!file.isEmpty()) {
            imageUploadService.saveFile(file, response.getId(), null);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // 판매글 목록 조회
    @GetMapping("/all/items")
    public ResponseEntity<List<ItemResponse>> showItems() {
        List<ItemResponse> response = itemSearchService.findAllItems();

        return ResponseEntity
                .ok(response);
    }

    // 판매글 상세 조회
    @GetMapping("/all/items/{itemId}")
    public ResponseEntity<ItemResponse> showOneItem(@PathVariable Long itemId) {
        ItemResponse itemResponse = itemSearchService.findOneItem(itemId);

        return ResponseEntity
                .ok(itemResponse);
    }

    // 판매글 수정
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ItemResponse> updateDescription(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                          @PathVariable Long itemId,
                                                          @Valid @RequestBody UpdateDescriptionRequest updateDescriptionRequest) {
        ItemResponse response = itemService.updateDescription(itemId, userId,
                updateDescriptionRequest);

        return ResponseEntity
                .ok(response);
    }

    // 판매글 삭제
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteOneItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                              @PathVariable Long itemId) {
        itemService.deletedById(itemId, userId);

        return ResponseEntity.
                noContent()
                .build();
    }

    // 72시간 이내 마감임박 상품 목록 조회
    @GetMapping("/all/items/impending")
    public ResponseEntity<List<?>> showImpendingItems() {
        List<ItemResponse> itemResponseList = itemSearchService.getImpendingItems();

        return ResponseEntity
                .ok(itemResponseList);

    }

    // 인기 상품 목록 조회
    @GetMapping("/all/items/popularity")
    public ResponseEntity<List<?>> showPopularity() {
        List<ItemResponse> itemResponseList = itemSearchService.findMostAddedToCartItems();

        return ResponseEntity
                .ok(itemResponseList);
    }

    // 상품 종류별 검색 목록 조회
    @GetMapping("/all/items/category/{categoryId}")
    public ResponseEntity<List<ItemWithImageUrlResponse>> showCategory(@PathVariable Category categoryId) {
        return ResponseEntity
                .ok(itemSearchService.getCategoryItems(categoryId));
    }

}

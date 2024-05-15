package com.gracefullyugly.domain.item.controller.api;

import com.gracefullyugly.domain.image.service.ImageUploadService;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.dto.ItemResponse;
import com.gracefullyugly.domain.item.dto.ItemWithImageUrlResponse;
import com.gracefullyugly.domain.item.dto.UpdateDescriptionRequest;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "상품 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;
    private final ItemSearchService itemSearchService;
    private final ImageUploadService imageUploadService;

    @Operation(summary = "상품 생성", description = "상품 판매글 생성하기")
    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('SELLER', 'ADMIN')")
    public ResponseEntity<ItemResponse> addItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                @ModelAttribute ItemRequest request,
                                                @RequestParam("productImage") MultipartFile file) {
        ItemResponse response = itemService.save(userId, request);

        if (!file.isEmpty()) {
            imageUploadService.saveFile(file, response.getId(), null);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response); //TODO 이미지 URL 추가
    }

    @Operation(summary = "상품 목록 조회", description = "상품 판매글 목록 조회하기")
    @GetMapping("/all/items")
    public ResponseEntity<List<ItemWithImageUrlResponse>> showItems() {
        return ResponseEntity
                .ok(itemSearchService.findAllItems());
    }

    @Operation(summary = "상품 상세 페이지", description = "상품 판매글 상세 페이지 조회하기")
    @GetMapping("/all/items/{itemId}")
    public ResponseEntity<ItemWithImageUrlResponse> showOneItem(@PathVariable Long itemId) {
        return ResponseEntity
                .ok(itemSearchService.findOneItem(itemId));
    }

    @Operation(summary = "상품 수정", description = "상품 판매글 설명 수정하기")
    @PutMapping("/items/{itemId}")
    public ResponseEntity<ItemResponse> updateDescription(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                          @PathVariable Long itemId,
                                                          @Valid @RequestBody UpdateDescriptionRequest updateDescriptionRequest) {
        ItemResponse response = itemService.updateDescription(itemId, userId,
                updateDescriptionRequest);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "상품 삭제", description = "상품 판매글 삭제하기")
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> deleteOneItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                              @PathVariable Long itemId) {
        itemService.deletedById(itemId, userId);

        return ResponseEntity.
                noContent()
                .build();
    }

    @Operation(summary = "마감임박 상품 목록 조회", description = "마감이 72시간 이내인 상품 목록 조회하기")
    @GetMapping("/all/items/impending")
    public ResponseEntity<List<ItemWithImageUrlResponse>> showImpendingItems() {
        return ResponseEntity
                .ok(itemSearchService.getImpendingItems());

    }

    @Operation(summary = "인기 상품 목록 조회", description = "찜 itemCount 개수 기준 상위 3개 상품만 조회하기")
    @GetMapping("/all/items/popularity")
    public ResponseEntity<List<ItemWithImageUrlResponse>> showPopularity() {
        return ResponseEntity
                .ok(itemSearchService.findMostAddedToCartItems());
    }

    @Operation(summary = "상품 종류별 검색 목록 조회", description = "채소, 과일, 기타 별로 목록 조회하기")
    @GetMapping("/all/items/category/{categoryId}")
    public ResponseEntity<List<ItemWithImageUrlResponse>> showCategory(@PathVariable Category categoryId) {
        return ResponseEntity
                .ok(itemSearchService.getCategoryItems(categoryId.getValue()));
    }

    // 상품명으로 검색
    @Operation(summary = "상품 검색", description = "상품명으로 검색하기")
    @GetMapping("/all/items/search")
    public ResponseEntity<List<ItemWithImageUrlResponse>> searchItems(@RequestParam String keyword) {
        return ResponseEntity
                .ok(itemSearchService.searchItemsByItemName(keyword));
    }

}

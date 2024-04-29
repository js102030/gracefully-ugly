package com.gracefullyugly.domain.item.controller;

import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.domain.item.dto.ItemRequestDto;
import com.gracefullyugly.domain.item.dto.ItemResponseDto;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 판매글 생성
    @PostMapping("/api/item")
    public ResponseEntity<ItemResponseDto> addItem(@RequestBody ItemRequestDto request) {
        Item item = itemService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(item.toResponse());
    }

    // 판매글 목록 조회
    @GetMapping("/api/items")
    public ResponseEntity<List<ItemResponseDto>> showItems() {
        List<Item> itemList = itemService.findAllItems();
        if (itemList == null || itemList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 빈 목록일 경우 noContent 상태 코드
        }
        List<ItemResponseDto> responseList = itemList.stream()
                .map(ItemResponseDto::new)
                .toList();
        return ResponseEntity.ok(responseList);
    }


    // 판매글 상세 조회
    @GetMapping("/api/item/{itemId}")
    public ResponseEntity<ItemResponseDto> showOneItem(@PathVariable Long itemId) {
        Item item = itemService.findOneItem(itemId);
        return ResponseEntity.ok(item.toResponse());

    }

    // 판매글 수정
    @PutMapping("/api/item/{itemId}")
    public ResponseEntity<ItemResponseDto> updateOneItem(@PathVariable Long itemId, @RequestBody ItemRequestDto request) {
        Item updateItem = itemService.update(itemId, request);
        return ResponseEntity.ok(updateItem.toResponse());
    }

    // 판매글 삭제
    @DeleteMapping("/api/item/{itemId}")
    public ResponseEntity<Void> deleteOneItem(@PathVariable Long itemId) {
        itemService.deletedById(itemId);
        return ResponseEntity.ok().build();
    }



}

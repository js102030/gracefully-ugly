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
import org.springframework.web.bind.annotation.*;

@RestController
public class ItemController {
    private final ItemService itemService;

    private ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // 판매글 생성
    @PostMapping("/api/item")
    public ResponseEntity<ItemResponseDto> addItem(@RequestBody ItemRequestDto request,
                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        Item item = itemService.save(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(item.toResponse());
    }




}

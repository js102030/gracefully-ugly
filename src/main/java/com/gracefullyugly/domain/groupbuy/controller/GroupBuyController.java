package com.gracefullyugly.domain.groupbuy.controller;

import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyListResponse;
import com.gracefullyugly.domain.groupbuy.service.GroupBuySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "특정 상품의 공동 구매")
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GroupBuyController {

    private GroupBuySearchService groupBuySearchService;

    @Operation(summary = "공동 구매 조회", description = "공동 구매 단건 조회")
    @GetMapping("/groupbuy/{groupBuyId}")
    public ResponseEntity<GroupBuyInfoResponse> getGroupBuyInfo(@PathVariable("groupBuyId") Long groupBuyId) {
        return ResponseEntity.ok(groupBuySearchService.getGroupBuyInfo(groupBuyId));
    }

    @Operation(summary = "특정 상품의 공동 구매 상태 조회", description = "공동 구매 최근 순으로 5건 조회")
    @GetMapping("/groupbuy/items/{itemId}")
    public ResponseEntity<GroupBuyListResponse> getGroupListByItemId(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(groupBuySearchService.getGroupBuyListByItemId(itemId));
    }
}

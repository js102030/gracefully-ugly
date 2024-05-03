package com.gracefullyugly.domain.groupbuy.controller;

import com.gracefullyugly.domain.groupbuy.dto.GroupBuyInfoResponse;
import com.gracefullyugly.domain.groupbuy.dto.GroupBuyListResponse;
import com.gracefullyugly.domain.groupbuy.service.GroupBuySearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GroupBuyController {

    private GroupBuySearchService groupBuySearchService;

    @GetMapping("/groupbuy/{groupBuyId}")
    public ResponseEntity<GroupBuyInfoResponse> getGroupBuyInfo(@PathVariable("groupBuyId") Long groupBuyId) {
        return ResponseEntity.ok(groupBuySearchService.getGroupBuyInfo(groupBuyId));
    }

    @GetMapping("/groupbuy/items/{itemId}")
    public ResponseEntity<GroupBuyListResponse> getGroupListByItemId(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(groupBuySearchService.getGroupBuyListByItemId(itemId));
    }
}

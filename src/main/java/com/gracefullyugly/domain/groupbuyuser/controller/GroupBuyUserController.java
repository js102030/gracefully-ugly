package com.gracefullyugly.domain.groupbuyuser.controller;

import com.gracefullyugly.domain.groupbuyuser.dto.GroupBuyUserFindResponse;
import com.gracefullyugly.domain.groupbuyuser.service.GroupBuyUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="특정 상품의 공동 구매 참여 정보")
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GroupBuyUserController {

    private GroupBuyUserService groupBuyUserService;

    @Operation(summary = "특정 상품의 공동 구매 참여 정보 조회", description = "공동 구매에 대한 자신의 참여 정보 제공")
    @GetMapping("/groupbuyuser/{itemId}")
    public ResponseEntity<List<GroupBuyUserFindResponse>> getGroupBuyUser(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(groupBuyUserService.getGroupBuyUser(userId, itemId));
    }
}

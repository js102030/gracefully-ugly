package com.gracefullyugly.domain.groupbuyuser.controller;

import com.gracefullyugly.domain.groupbuyuser.dto.GroupBuyUserFindResponse;
import com.gracefullyugly.domain.groupbuyuser.service.GroupBuyUserService;
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

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class GroupBuyUserController {

    private GroupBuyUserService groupBuyUserService;

    @GetMapping("/groupbuyuser/{itemId}")
    public ResponseEntity<List<GroupBuyUserFindResponse>> getGroupBuyUser(
            @Valid @NotNull @AuthenticationPrincipal(expression = "userId") Long userId,
            @PathVariable("itemId") Long itemId) {
        return ResponseEntity.ok(groupBuyUserService.getGroupBuyUser(userId, itemId));
    }
}

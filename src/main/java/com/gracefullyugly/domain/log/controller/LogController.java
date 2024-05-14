package com.gracefullyugly.domain.log.controller;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.log.entity.Log;
import com.gracefullyugly.domain.log.service.LogSearchService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="로그 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogController {

    private final LogSearchService logSearchService;

    @Operation(summary = "유저 로그 조회", description = "유저 로그 조회하기")
    @GetMapping("/logs/users/{userId}")
    public ResponseEntity<ApiResponse<List<Log>>> findLogsByUserId(@PathVariable Long userId) {
        ApiResponse<List<Log>> response = logSearchService.findLogsByUserId(userId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "로그 단건 조회", description = "로그 단건 조회하기")
    @GetMapping("/logs/{logId}")
    public ResponseEntity<Log> findByLogId(@PathVariable Long logId) {
        Log log = logSearchService.findByLogId(logId);

        return ResponseEntity
                .ok(log);
    }

}

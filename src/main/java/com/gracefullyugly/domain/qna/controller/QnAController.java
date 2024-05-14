package com.gracefullyugly.domain.qna.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.dto.QuestionDto;
import com.gracefullyugly.domain.qna.service.QnASearchService;
import com.gracefullyugly.domain.qna.service.QnAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="상품 문의 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnAController {

    private final QnAService qnAService;
    private final QnASearchService qnASearchService;

    @Operation(summary = "상품 문의 생성", description = "구매자가 상품 문의 댓글을 생성함")
    @PostMapping("/questions/{itemId}")
    public ResponseEntity<QnADto> createQnA(@AuthenticationPrincipal(expression = "userId") Long userId,
                                            @PathVariable Long itemId, @Valid @RequestBody QuestionDto request) {
        QnADto response = qnAService.createQnA(userId, itemId, request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @Operation(summary = "판매자 답글 생성", description = "판매자가 상품 문의 답글을 생성함")
    @PostMapping("/answers/{qnaId}")
    public ResponseEntity<QnADto> createAnswer(@AuthenticationPrincipal(expression = "userId") Long userId,
                                               @PathVariable Long qnaId, @Valid @RequestBody AnswerDto request) {
        QnADto response = qnAService.createAnswer(userId, qnaId, request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @Operation(summary = "상품 문의 + 답글 조회", description = "상품 문의와 특정 답글 조회")
    @GetMapping("/questions/{qnaId}")
    public ResponseEntity<QnADto> readQnA(@PathVariable Long qnaId) {
        QnADto qnADto = qnASearchService.getQnA(qnaId);

        return ResponseEntity
                .ok(qnADto);
    }

    @Operation(summary = "상품 문의 + 답글 목록 조회", description = "상품 문의와 답글 목록 조회")
    @GetMapping("/questions/items/{itemId}")
    public ResponseEntity<ApiResponse<List<QnADto>>> readQnAs(@PathVariable Long itemId) {
        ApiResponse<List<QnADto>> response = qnASearchService.getQnAList(itemId);

        return ResponseEntity
                .ok(response);
    }

}

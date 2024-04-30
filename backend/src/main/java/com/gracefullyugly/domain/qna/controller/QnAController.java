package com.gracefullyugly.domain.qna.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.dto.QuestionDto;
import com.gracefullyugly.domain.qna.service.QnASearchService;
import com.gracefullyugly.domain.qna.service.QnAService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QnAController {

    private final QnAService qnAService;
    private final QnASearchService qnASearchService;

    @PostMapping("/questions/{itemId}")
    public ResponseEntity<QuestionDto> createQnA(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                 @PathVariable Long itemId, @Valid @RequestBody QuestionDto request) {
        QuestionDto qnADto = qnAService.createQnA(userId, itemId, request);

        return ResponseEntity
                .status(CREATED)
                .body(qnADto);
    }

    @PostMapping("answers/{qnaId}")
    public ResponseEntity<AnswerDto> createAnswer(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                  @PathVariable Long qnaId, @Valid @RequestBody AnswerDto request) {
        AnswerDto answerDto = qnAService.createAnswer(userId, qnaId, request);

        return ResponseEntity
                .status(CREATED)
                .body(answerDto);
    }

    @GetMapping("/questions/{qnaId}")
    public ResponseEntity<QnADto> readQnA(@PathVariable Long qnaId) {
        QnADto qnADto = qnASearchService.readQnA(qnaId);

        return ResponseEntity
                .ok(qnADto);
    }

    @GetMapping("/questions/{itemId}")
    public ResponseEntity<ApiResponse<List<QnADto>>> readQnAs(@PathVariable Long itemId) {
        ApiResponse<List<QnADto>> response = qnASearchService.readQnAs(itemId);

        return ResponseEntity
                .ok(response);
    }

}

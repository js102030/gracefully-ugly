package com.gracefullyugly.domain.qna.service;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.service.ItemSearchService;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import com.gracefullyugly.domain.qna.dto.QuestionDto;
import com.gracefullyugly.domain.qna.entity.QnA;
import com.gracefullyugly.domain.qna.repository.QnARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QnAService {

    private final QnARepository qnARepository;
    private final QnASearchService qnASearchService;
    private final ItemSearchService itemSearchService;

    public QuestionDto createQnA(Long userId, Long itemId, QuestionDto request) {
        QnA QnAEntity = request.toEntity(userId, itemId);

        QnA savedQnA = qnARepository.save(QnAEntity);

        return new QuestionDto(savedQnA.getQuestion());
    }

    public AnswerDto createAnswer(Long userId, Long questionId, AnswerDto request) {
        QnA findQnA = qnASearchService.findById(questionId);

        Item findItem = itemSearchService.findById(findQnA.getItemId());

        if (!findItem.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 상품의 판매자만 답변을 작성할 수 있습니다.");
        }

        return findQnA.addAnswer(request.getAnswer());
    }
}

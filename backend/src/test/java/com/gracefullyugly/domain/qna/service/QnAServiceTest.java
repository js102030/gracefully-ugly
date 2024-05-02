package com.gracefullyugly.domain.qna.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.dto.QuestionDto;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
class QnAServiceTest {

    @Autowired
    private QnAService qnAService;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        log.info("before each");
    }

    @Test
    @DisplayName("QnA 생성 테스트")
    void createQnA() {
        // given
        Long userId = 1L;
        Long itemId = 1L;
        QuestionDto request = new QuestionDto("질문 내용");

        // when
        QnADto qnaDto = qnAService.createQnA(userId, itemId, request);

        // then
        assertThat(qnaDto.getQuestion()).isEqualTo(request.getQuestion());
    }

    @Test
    @DisplayName("QnA 답변 생성 테스트")
    void createAnswer() {
        // given
        Long userId = 1L;
        QuestionDto request = new QuestionDto("질문 내용");

        Item savedItem = itemRepository.save(
                Item.builder()
                        .userId(userId)
                        .categoryId(Category.FRUIT)
                        .name("사과")
                        .description("맛있는 사과")
                        .price(1000)
                        .totalSalesUnit(100)
                        .minUnitWeight(100)
                        .minGroupBuyWeight(100)
                        .productionPlace("한국")
                        .closedDate(LocalDateTime.now().plusDays(3))
                        .build()
        );

        // when
        QnADto qnaDto = qnAService.createQnA(userId, savedItem.getId(), request);
        QnADto qnaDtoWithAnswer = qnAService.createAnswer(userId, qnaDto.getQnaId(), new AnswerDto("답변 내용"));

        // then
        assertThat(qnaDtoWithAnswer.getAnswer()).isEqualTo("답변 내용");
    }

}
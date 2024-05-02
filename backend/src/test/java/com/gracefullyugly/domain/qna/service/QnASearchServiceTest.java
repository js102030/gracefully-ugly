package com.gracefullyugly.domain.qna.service;

import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.entity.QnA;
import com.gracefullyugly.domain.qna.repository.QnARepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QnASearchServiceTest {

    @Autowired
    QnASearchService qnASearchService;

    @Autowired
    QnARepository qnARepository;

    @Test
    @DisplayName("QnA 조회 테스트")
    void getQnATest() {
        QnA qnA = QnA.builder()
                .userId(1L)
                .itemId(1L)
                .question("QnA 내용")
                .build();

        QnA savedQnA = qnARepository.save(qnA);

        QnADto response = qnASearchService.getQnA(savedQnA.getId());

        Assertions.assertEquals(savedQnA.getId(), response.getQnaId());
        Assertions.assertEquals(savedQnA.getUserId(), response.getUserId());
        Assertions.assertEquals(savedQnA.getItemId(), response.getItemId());
        Assertions.assertEquals(savedQnA.getQuestion(), response.getQuestion());
        Assertions.assertEquals(savedQnA.getAnswer(), response.getAnswer());
    }

    @Test
    @DisplayName("QnA 목록 조회 테스트")
    void getQnAListTest() {
        QnA qnA1 = QnA.builder()
                .userId(1L)
                .itemId(1L)
                .question("QnA 내용")
                .build();
        QnA qnA2 = QnA.builder()
                .userId(2L)
                .itemId(1L)
                .question("QnA 내용")
                .build();

        QnA savedQnA1 = qnARepository.save(qnA1);
        QnA savedQnA2 = qnARepository.save(qnA2);

        List<QnADto> response = qnASearchService.getQnAList(1L).getData();

        Assertions.assertEquals(2, response.size());
        Assertions.assertEquals(savedQnA1.getId(), response.get(0).getQnaId());
        Assertions.assertEquals(savedQnA1.getUserId(), response.get(0).getUserId());
        Assertions.assertEquals(savedQnA1.getItemId(), response.get(0).getItemId());
        Assertions.assertEquals(savedQnA1.getQuestion(), response.get(0).getQuestion());
        Assertions.assertEquals(savedQnA1.getAnswer(), response.get(0).getAnswer());

        Assertions.assertEquals(savedQnA2.getId(), response.get(1).getQnaId());
        Assertions.assertEquals(savedQnA2.getUserId(), response.get(1).getUserId());
        Assertions.assertEquals(savedQnA2.getItemId(), response.get(1).getItemId());
        Assertions.assertEquals(savedQnA2.getQuestion(), response.get(1).getQuestion());
        Assertions.assertEquals(savedQnA2.getAnswer(), response.get(1).getAnswer());

    }
}
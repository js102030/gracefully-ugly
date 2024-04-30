package com.gracefullyugly.domain.qna.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class QnA extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_id")
    private Long id;

    private Long userId;

    private Long itemId;

    private String question;

    private String answer;

    @Builder
    public QnA(Long userId, Long itemId, String question) {
        this.userId = userId;
        this.itemId = itemId;
        this.question = question;
    }

    public AnswerDto addAnswer(String answer) {
        this.answer = answer;
        return new AnswerDto(answer);
    }
}

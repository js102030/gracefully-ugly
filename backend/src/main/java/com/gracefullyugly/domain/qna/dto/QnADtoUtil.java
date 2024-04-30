package com.gracefullyugly.domain.qna.dto;

import com.gracefullyugly.domain.qna.entity.QnA;

public class QnADtoUtil {

    public static QnADto QnAToQnADto(QnA qna) {

        return QnADto.builder()
                .userId(qna.getUserId())
                .question(qna.getQuestion())
                .answer(qna.getAnswer())
                .build();

    }
}

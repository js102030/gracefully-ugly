package com.gracefullyugly.domain.qna.dto;

import com.gracefullyugly.domain.qna.entity.QnA;

public class QnADtoUtil {

    public static QnADto qnAToQnADto(QnA qna) {

        return QnADto.builder()
                .qnaId(qna.getId())
                .userId(qna.getUserId())
                .itemId(qna.getItemId())
                .question(qna.getQuestion())
                .answer(qna.getAnswer())
                .build();

    }
}

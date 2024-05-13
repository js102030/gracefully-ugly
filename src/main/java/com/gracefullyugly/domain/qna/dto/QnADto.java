package com.gracefullyugly.domain.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QnADto {

    private Long qnaId;
    private Long userId;
    private Long itemId;
    private String question;
    private String answer;

}

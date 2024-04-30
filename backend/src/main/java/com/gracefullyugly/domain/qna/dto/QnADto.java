package com.gracefullyugly.domain.qna.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class QnADto {

    private Long userId;
    private String question;
    private String answer;

}

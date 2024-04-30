package com.gracefullyugly.domain.qna.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AnswerDto {

    @NotBlank(message = "답변은 필수입니다.")
    private String answer;

}

package com.gracefullyugly.domain.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReportRequest {

    @NotBlank(message = "신고 내용은 필수입니다.")
    private String contents;

}



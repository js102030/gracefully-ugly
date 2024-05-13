package com.gracefullyugly.domain.report.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReportResponse {

    private Long reportId;
    private Long userId;
    private Long itemId;
    private Long reviewId;
    private String comments;
    private boolean isAccepted;
    private boolean isDeleted;
    private LocalDateTime createdDate;

}

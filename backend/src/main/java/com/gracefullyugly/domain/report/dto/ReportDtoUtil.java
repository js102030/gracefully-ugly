package com.gracefullyugly.domain.report.dto;

import com.gracefullyugly.domain.report.entity.Report;

public class ReportDtoUtil {

    public static ReportResponse reportToReportResponse(Report report) {

        return ReportResponse.builder()
                .reportId(report.getId())
                .userId(report.getUserId())
                .itemId(report.getItemId())
                .reviewId(report.getReviewId())
                .comments(report.getComments())
                .isAccepted(report.isAccepted())
                .isDeleted(report.isDeleted())
                .createdDate(report.getCreatedDate())
                .build();

    }

}

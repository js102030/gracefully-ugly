package com.gracefullyugly.domain.report.service;

import com.gracefullyugly.common.exception.custom.NotFoundException;
import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.report.dto.ReportDtoUtil;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.entity.Report;
import com.gracefullyugly.domain.report.repository.ReportRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportSearchService {

    private final ReportRepository reportRepository;

    public ReportResponse getItemReport(Long itemId) {
        Report findReport = findByItemId(itemId);

        return ReportDtoUtil.reportToReportResponse(findReport);
    }

    public ReportResponse getReviewReport(Long reviewId) {
        Report findReport = findByReviewId(reviewId);

        return ReportDtoUtil.reportToReportResponse(findReport);
    }

    public ApiResponse<List<ReportResponse>> getItemReports() {
        List<Report> findReports = reportRepository.findByReviewIdIsNull();

        if (findReports.isEmpty()) {
            throw new IllegalArgumentException("신고된 상품이 없습니다.");
        }

        List<ReportResponse> responses = findReports
                .stream()
                .map(ReportDtoUtil::reportToReportResponse)
                .toList();

        return new ApiResponse<>(responses.size(), responses);

    }

    public ApiResponse<List<ReportResponse>> getReviewReports() {
        List<Report> findReports = reportRepository.findByItemIdIsNull();

        if (findReports.isEmpty()) {
            throw new IllegalArgumentException("신고된 리뷰가 없습니다.");
        }

        List<ReportResponse> responses = findReports
                .stream()
                .map(ReportDtoUtil::reportToReportResponse)
                .toList();

        return new ApiResponse<>(responses.size(), responses);
    }

    public Report findById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("reportId : " + reportId + "에 해당하는 신고가 없습니다."));
    }

    private Report findByReviewId(Long reviewId) {
        return reportRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("reviewId : " + reviewId + "에 해당하는 신고가 없습니다."));
    }

    private Report findByItemId(Long itemId) {
        return reportRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("itemId : " + itemId + "에 해당하는 신고가 없습니다."));
    }

    public List<ReportResponse> getReports() {
        List<Report> reports = reportRepository.findByIsAcceptedFalseAndIsDeletedFalse();

        if (reports.isEmpty()) {
            throw new NotFoundException("신고된 내용이 없습니다.");
        }

        return reports
                .stream()
                .map(ReportDtoUtil::reportToReportResponse)
                .toList();
    }
}

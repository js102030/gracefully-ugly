package com.gracefullyugly.domain.report.service;

import com.gracefullyugly.domain.report.dto.ReportDtoUtil;
import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.entity.Report;
import com.gracefullyugly.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportSearchService reportSearchService;
    private final ReportRepository reportRepository;

    public ReportResponse reportItem(Long userId, Long itemId, ReportRequest request) {
        Report report = Report.forItem(userId, itemId, request.getContents());

        Report savedReport = reportRepository.save(report);

        return ReportDtoUtil.reportToReportResponse(savedReport);
    }

    public ReportResponse reportReview(Long userId, Long reviewId, ReportRequest request) {
        Report report = Report.forReview(userId, reviewId, request.getContents());

        Report savedReport = reportRepository.save(report);

        return ReportDtoUtil.reportToReportResponse(savedReport);
    }

    public void acceptReport(Long reportId) {
        Report findReport = reportSearchService.findById(reportId);

        findReport.accept();

        if (findReport.isItemReport()) {
            Long itemId = findReport.getItemId();
            reportRepository.deleteCartItemByItemId(itemId);
        } else {
            //TODO 후기 신고 처리
        }
    }

    public void deleteReport(Long reportId) {
        Report findReport = reportSearchService.findById(reportId);

        findReport.delete();
    }

}

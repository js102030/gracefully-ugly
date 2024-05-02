package com.gracefullyugly.domain.report.service;

import com.gracefullyugly.domain.report.dto.ReportDtoUtil;
import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.entity.Report;
import com.gracefullyugly.domain.report.repository.ReportRepository;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportSearchService reportSearchService;
    private final ReportRepository reportRepository;
    private final UserSearchService userSearchService;

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

    public void acceptReport(Long userId, Long reportId) {
        User findUser = userSearchService.findById(userId);

        validAdmin(findUser);

        Report findReport = reportSearchService.findById(reportId);

        findReport.accept();
    }

    public void deleteReport(Long userId, Long reportId) {
        User findUser = userSearchService.findById(userId);

        validAdmin(findUser);

        Report findReport = reportSearchService.findById(reportId);

        findReport.delete();
    }

    private void validAdmin(User findUser) {
        if (findUser.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

}

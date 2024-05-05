package com.gracefullyugly.domain.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.entity.Report;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Autowired
    ReportSearchService reportSearchService;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("상품 신고 테스트")
    void reportItemTest() {
        // WHEN
        ReportResponse response = reportService.reportItem(1L, 1L, new ReportRequest("신고 내용"));

        // THEN
        assertEquals(response.getComments(), "신고 내용");
        assertEquals(response.getItemId(), 1L);
        assertEquals(response.getUserId(), 1L);
    }

    @Test
    @DisplayName("리뷰 신고 테스트")
    void reportReviewTest() {
        // WHEN
        ReportResponse response = reportService.reportReview(1L, 1L, new ReportRequest("신고 내용"));

        // THEN
        assertEquals(response.getComments(), "신고 내용");
        assertEquals(response.getReviewId(), 1L);
        assertEquals(response.getUserId(), 1L);
    }

    @Test
    @DisplayName("신고 승인 테스트")
    void acceptReportTest() {
        User build = User.builder()
                .userId(100L)
                .role(Role.ADMIN)
                .loginId("admin")
                .password("admin")
                .build();
        User savedUser = userRepository.save(build);

        ReportResponse response = reportService.reportItem(savedUser.getId(), 1L, new ReportRequest("신고 내용"));

        // WHEN
        reportService.acceptReport(response.getReportId());

        // THEN
        Report findReport = reportSearchService.findById(response.getReportId());
        assertTrue(findReport.isAccepted());
    }

    @Test
    @DisplayName("신고 삭제 테스트")
    void deleteReportTest() {
        User build = User.builder()
                .userId(100L)
                .role(Role.ADMIN)
                .loginId("admin")
                .password("admin")
                .build();
        User savedUser = userRepository.save(build);

        ReportResponse response = reportService.reportItem(savedUser.getId(), 1L, new ReportRequest("신고 내용"));

        // WHEN
        reportService.deleteReport(response.getReportId());

        // THEN
        Report findReport = reportSearchService.findById(response.getReportId());
        assertTrue(findReport.isDeleted());
    }

}
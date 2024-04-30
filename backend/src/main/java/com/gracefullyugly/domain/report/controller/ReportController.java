package com.gracefullyugly.domain.report.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.service.ReportSearchService;
import com.gracefullyugly.domain.report.service.ReportService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;
    private final ReportSearchService reportSearchService;

    @PostMapping("/report/items/{itemId}")
    public ResponseEntity<ReportResponse> reportItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                     @PathVariable Long itemId,
                                                     @Valid @RequestPart ReportRequest request) {
        final ReportResponse response = reportService.reportItem(userId, itemId, request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @PostMapping("/report/reviews/{reviewId}")
    public ResponseEntity<ReportResponse> reportReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                       @PathVariable Long reviewId,
                                                       @Valid @RequestPart ReportRequest request) {
        final ReportResponse response = reportService.reportReview(userId, reviewId, request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @GetMapping("/report/items/{itemId}")
    public ResponseEntity<ReportResponse> getItemReport(@PathVariable Long itemId) {
        final ReportResponse response = reportSearchService.getItemReport(itemId);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/report/reviews/{reviewId}")
    public ResponseEntity<ReportResponse> getReviewReport(@PathVariable Long reviewId) {
        final ReportResponse response = reportSearchService.getReviewReport(reviewId);

        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/report/items")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getItemReports() {
        final ApiResponse<List<ReportResponse>> response = reportSearchService.getItemReports();

        return ResponseEntity
                .ok(response);
    }

    @GetMapping("/report/reviews")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReviewReports() {
        final ApiResponse<List<ReportResponse>> response = reportSearchService.getReviewReports();

        return ResponseEntity
                .ok(response);
    }

    @PatchMapping("/report/{reportId}/status")
    public ResponseEntity<Void> updateReportStatus(@PathVariable Long reportId) {
        reportService.updateReportStatus(reportId);

        return ResponseEntity
                .ok()
                .build();
    }

    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);

        return ResponseEntity
                .noContent()
                .build();
    }


}

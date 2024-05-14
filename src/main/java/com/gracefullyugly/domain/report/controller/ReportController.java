package com.gracefullyugly.domain.report.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.service.ReportSearchService;
import com.gracefullyugly.domain.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="상품 및 후기 신고 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;
    private final ReportSearchService reportSearchService;

    @Operation(summary = "상품 신고", description = "상품 판매글을 신고함")
    @PostMapping("/report/items/{itemId}")
    public ResponseEntity<ReportResponse> reportItem(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                     @PathVariable Long itemId,
                                                     @Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.reportItem(userId, itemId, request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @Operation(summary = "후기 신고", description = "상품 후기글을 신고함")
    @PostMapping("/report/reviews/{reviewId}")
    public ResponseEntity<ReportResponse> reportReview(@AuthenticationPrincipal(expression = "userId") Long userId,
                                                       @PathVariable Long reviewId,
                                                       @Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.reportReview(userId, reviewId, request);

        return ResponseEntity
                .status(CREATED)
                .body(response);
    }

    @Operation(summary = "상품 신고 단건 조회", description = "상품 판매 신고글을 단건 조회함")
    @GetMapping("/report/items/{itemId}")
    public ResponseEntity<ReportResponse> getItemReport(@PathVariable Long itemId) {
        ReportResponse response = reportSearchService.getItemReport(itemId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "후기 신고 단건 조회", description = "상품 후기 신고글을 단건 조회함")
    @GetMapping("/report/reviews/{reviewId}")
    public ResponseEntity<ReportResponse> getReviewReport(@PathVariable Long reviewId) {
        ReportResponse response = reportSearchService.getReviewReport(reviewId);

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "상품 신고 목록 조회", description = "상품 판매 신고글을 목록 조회함")
    @GetMapping("/report/items")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getItemReports() {
        ApiResponse<List<ReportResponse>> response = reportSearchService.getItemReports();

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "후기 신고 목록 조회", description = "상품 후기 신고글을 목록 조회함")
    @GetMapping("/report/reviews")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReviewReports() {
        ApiResponse<List<ReportResponse>> response = reportSearchService.getReviewReports();

        return ResponseEntity
                .ok(response);
    }

    @Operation(summary = "신고 승인", description = "관리자가 신고글의 신고를 승인함")
    @PatchMapping("/report/{reportId}/accept")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> acceptReport(@PathVariable Long reportId) {
        reportService.acceptReport(reportId);

        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "신고 삭제", description = "관리자가 신고글의 신고를 삭제함")
    @DeleteMapping("/report/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        reportService.deleteReport(reportId);

        return ResponseEntity
                .noContent()
                .build();
    }


}

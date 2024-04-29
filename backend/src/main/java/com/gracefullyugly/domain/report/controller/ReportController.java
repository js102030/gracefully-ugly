//package com.gracefullyugly.domain.report.controller;
//
//import com.gracefullyugly.common.security.CustomUserDetails;
//import com.gracefullyugly.domain.report.dto.ReportItemRequestDto;
//import com.gracefullyugly.domain.report.dto.ReportItemResponseDto;
//import com.gracefullyugly.domain.report.entity.Report;
//import com.gracefullyugly.domain.report.service.ReportService;
//import io.swagger.v3.oas.annotations.Operation;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ReportController {
//    private final ReportService reportService;
//    public ReportController(ReportService reportService) {
//        this.reportService = reportService;
//    }
//
//    @Operation(summary = "상품 신고 생성", description = "상품 신고글 생성하기")
//    @PostMapping("/api/report/item")
//    public ResponseEntity<?> addReportItem (@RequestPart ReportItemRequestDto request,
//                                            @AuthenticationPrincipal CustomUserDetails customUserDetails
//                                            ) {
//        Long userId = customUserDetails.getUserId();
//        Report report = reportService.saveReportItem(request, userId);
//        ReportItemResponseDto response = report.toResponse();
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//}

//package com.gracefullyugly.domain.report.controller;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.google.gson.Gson;
//import com.gracefullyugly.common.security.jwt.JWTUtil;
//import com.gracefullyugly.common.wrapper.ApiResponse;
//import com.gracefullyugly.domain.report.dto.ReportRequest;
//import com.gracefullyugly.domain.report.dto.ReportResponse;
//import com.gracefullyugly.domain.report.service.ReportSearchService;
//import com.gracefullyugly.domain.report.service.ReportService;
//import java.time.LocalDateTime;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Slf4j
//class ReportControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReportService reportService;
//
//    @MockBean
//    private ReportSearchService reportSearchService;
//
//    @Autowired
//    private JWTUtil jwtUtil;
//
//    @Test
//    @DisplayName("아이템 신고 테스트")
//    void reportItemTest() throws Exception {
//        // given
//        ReportResponse reportResponse = new ReportResponse(
//                1L,
//                1L,
//                1L,
//                null,
//                "신고 내용",
//                false,
//                false,
//                LocalDateTime.now());
//
//        given(reportService.reportItem(any(), any(), any())).willReturn(
//                reportResponse
//        );
//
//        ReportRequest reportRequest = new ReportRequest("신고 내용");
//
//        Gson gson = new Gson();
//        String json = gson.toJson(reportRequest);
//
//        String access = getToken();
//
//        // when then
//        mockMvc.perform(post("/api/report/items/1")
//                        .header("access", access)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.reportId").value(1L))
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.itemId").value(1L))
//                .andExpect(jsonPath("$.comments").value("신고 내용"))
//                .andExpect(jsonPath("$.deleted").value(false))
//                .andExpect(jsonPath("$.accepted").value(false))
//                .andExpect(jsonPath("$.createdDate").isNotEmpty())
//                .andDo(print());
//
//        verify(reportService).reportItem(any(), any(), any());
//    }
//
//    @Test
//    @DisplayName("리뷰 신고 테스트")
//    void reportReviewTest() throws Exception {
//        // given
//        ReportResponse reportResponse = new ReportResponse(
//                1L,
//                1L,
//                null,
//                1L,
//                "신고 내용",
//                false,
//                false,
//                LocalDateTime.now());
//
//        given(reportService.reportReview(any(), any(), any())).willReturn(
//                reportResponse
//        );
//
//        ReportRequest reportRequest = new ReportRequest("신고 내용");
//
//        Gson gson = new Gson();
//        String json = gson.toJson(reportRequest);
//
//        String access = getToken();
//
//        // when then
//        mockMvc.perform(post("/api/report/reviews/1")
//                        .header("access", access)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.reportId").value(1L))
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.reviewId").value(1L))
//                .andExpect(jsonPath("$.comments").value("신고 내용"))
//                .andExpect(jsonPath("$.deleted").value(false))
//                .andExpect(jsonPath("$.accepted").value(false))
//                .andExpect(jsonPath("$.createdDate").isNotEmpty())
//                .andDo(print());
//
//        verify(reportService).reportReview(any(), any(), any());
//    }
//
//    @Test
//    @DisplayName("아이템 신고 조회 테스트")
//    void getItemReportTest() throws Exception {
//        // given
//        ReportResponse reportResponse = new ReportResponse(
//                1L,
//                1L,
//                1L,
//                null,
//                "신고 내용",
//                false,
//                false,
//                LocalDateTime.now());
//
//        given(reportSearchService.getItemReport(any())).willReturn(
//                reportResponse
//        );
//
//        // when then
//        mockMvc.perform(get("/api/report/items/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.reportId").value(1L))
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.itemId").value(1L))
//                .andExpect(jsonPath("$.comments").value("신고 내용"))
//                .andExpect(jsonPath("$.deleted").value(false))
//                .andExpect(jsonPath("$.accepted").value(false))
//                .andExpect(jsonPath("$.createdDate").isNotEmpty())
//                .andDo(print());
//
//        verify(reportSearchService).getItemReport(any());
//    }
//
//    @Test
//    @DisplayName("리뷰 신고 조회 테스트")
//    void getReviewReportTest() throws Exception {
//        // given
//        ReportResponse reportResponse = new ReportResponse(
//                1L,
//                1L,
//                null,
//                1L,
//                "신고 내용",
//                false,
//                false,
//                LocalDateTime.now());
//
//        given(reportSearchService.getReviewReport(any())).willReturn(
//                reportResponse
//        );
//
//        // when then
//        mockMvc.perform(get("/api/report/reviews/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.reportId").value(1L))
//                .andExpect(jsonPath("$.userId").value(1L))
//                .andExpect(jsonPath("$.reviewId").value(1L))
//                .andExpect(jsonPath("$.comments").value("신고 내용"))
//                .andExpect(jsonPath("$.deleted").value(false))
//                .andExpect(jsonPath("$.accepted").value(false))
//                .andExpect(jsonPath("$.createdDate").isNotEmpty())
//                .andDo(print());
//
//        verify(reportSearchService).getReviewReport(any());
//    }
//
//    @Test
//    @DisplayName("아이템 신고 목록 조회 테스트")
//    void getItemReportsTest() throws Exception {
//        // given
//        ReportResponse reportResponse1 = new ReportResponse(
//                1L,
//                1L,
//                1L,
//                null,
//                "신고 내용",
//                false,
//                false,
//                LocalDateTime.now());
//        ReportResponse reportResponse2 = new ReportResponse(
//                2L,
//                2L,
//                2L,
//                null,
//                "신고 내용2",
//                false,
//                false,
//                LocalDateTime.now());
//
//        List<ReportResponse> list = List.of(reportResponse1, reportResponse2);
//
//        given(reportSearchService.getItemReports()).willReturn(
//                new ApiResponse<>(list.size(), list)
//        );
//
//        // when then
//        mockMvc.perform(get("/api/report/items"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].reportId").value(1L))
//                .andExpect(jsonPath("$.data[0].userId").value(1L))
//                .andExpect(jsonPath("$.data[0].itemId").value(1L))
//                .andExpect(jsonPath("$.data[0].comments").value("신고 내용"))
//                .andExpect(jsonPath("$.data[0].deleted").value(false))
//                .andExpect(jsonPath("$.data[0].accepted").value(false))
//                .andExpect(jsonPath("$.data[0].createdDate").isNotEmpty())
//                .andExpect(jsonPath("$.data[1].reportId").value(2L))
//                .andExpect(jsonPath("$.data[1].userId").value(2L))
//                .andExpect(jsonPath("$.data[1].itemId").value(2L))
//                .andExpect(jsonPath("$.data[1].comments").value("신고 내용2"))
//                .andExpect(jsonPath("$.data[1].deleted").value(false))
//                .andExpect(jsonPath("$.data[1].accepted").value(false))
//                .andExpect(jsonPath("$.data[1].createdDate").isNotEmpty())
//                .andDo(print());
//
//        verify(reportSearchService).getItemReports();
//    }
//
//    @Test
//    @DisplayName("리뷰 신고 목록 조회 테스트")
//    void getReviewReportsTest() throws Exception {
//        // given
//        ReportResponse reportResponse1 = new ReportResponse(
//                1L,
//                1L,
//                null,
//                1L,
//                "신고 내용",
//                false,
//                false,
//                LocalDateTime.now());
//        ReportResponse reportResponse2 = new ReportResponse(
//                2L,
//                2L,
//                null,
//                2L,
//                "신고 내용2",
//                false,
//                false,
//                LocalDateTime.now());
//
//        List<ReportResponse> list = List.of(reportResponse1, reportResponse2);
//
//        given(reportSearchService.getReviewReports()).willReturn(
//                new ApiResponse<>(list.size(), list)
//        );
//
//        // when then
//        mockMvc.perform(get("/api/report/reviews"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].reportId").value(1L))
//                .andExpect(jsonPath("$.data[0].userId").value(1L))
//                .andExpect(jsonPath("$.data[0].reviewId").value(1L))
//                .andExpect(jsonPath("$.data[0].comments").value("신고 내용"))
//                .andExpect(jsonPath("$.data[0].deleted").value(false))
//                .andExpect(jsonPath("$.data[0].accepted").value(false))
//                .andExpect(jsonPath("$.data[0].createdDate").isNotEmpty())
//                .andExpect(jsonPath("$.data[1].reportId").value(2L))
//                .andExpect(jsonPath("$.data[1].userId").value(2L))
//                .andExpect(jsonPath("$.data[1].reviewId").value(2L))
//                .andExpect(jsonPath("$.data[1].comments").value("신고 내용2"))
//                .andExpect(jsonPath("$.data[1].deleted").value(false))
//                .andExpect(jsonPath("$.data[1].accepted").value(false))
//                .andExpect(jsonPath("$.data[1].createdDate").isNotEmpty())
//                .andDo(print());
//
//        verify(reportSearchService).getReviewReports();
//    }
//
//    @Test
//    @DisplayName("신고 승인 테스트")
//    void acceptReportTest() throws Exception {
//        // given
//        String access = getToken();
//
//        // when then
//        mockMvc.perform(patch("/api/report/1/accept")
//                        .header("access", access))
//                .andExpect(status().isOk())
//                .andDo(print());
//
//        verify(reportService).acceptReport(any());
//    }
//
//    @Test
//    @DisplayName("신고 삭제 테스트")
//    void deleteReportTest() throws Exception {
//        // given
//        String access = getToken();
//
//        // when then
//        mockMvc.perform(delete("/api/report/1")
//                        .header("access", access))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        verify(reportService).deleteReport(any());
//    }
//
//
//    private String getToken() {
//        return jwtUtil.createJwt(100L, "loginId", "ROLE_SELLER", 60 * 10 * 1000L, null);
//    }
//}
package com.gracefullyugly.domain.report.controller;

import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.report.service.ReportSearchService;
import com.gracefullyugly.domain.report.service.ReportService;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testuserdetails.TestUserDetailsService;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private ReportSearchService reportSearchService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private TestUserDetailsService testUserDetailsService;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setupTestData() {
        // 회원 정보 세팅
        userRepository.save(SetupDataUtils.makeTestSellerUser((passwordEncoder)));

        // 상품 정보 세팅
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(1));

        // UserDetails 세팅
        testUserDetailsService = new TestUserDetailsService(userRepository);
        customUserDetails = (CustomUserDetails) testUserDetailsService.loadUserByUsername(TEST_LOGIN_ID);
    }

    @AfterEach
    void deleteTestData() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("아이템 신고 테스트")
    void reportItemTest() throws Exception {
        // given
        ReportResponse reportResponse = new ReportResponse(
                1L,
                1L,
                1L,
                null,
                "신고 내용",
                false,
                false,
                LocalDateTime.now());

        given(reportService.reportItem(any(), any(), any())).willReturn(
                reportResponse
        );

        ReportRequest reportRequest = new ReportRequest("신고 내용");

        Gson gson = new Gson();
        String json = gson.toJson(reportRequest);

        String access = getToken();

        // when then
        mockMvc.perform(post("/api/report/items/1")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(customUserDetails)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.comments").value("신고 내용"))
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andDo(print());

        verify(reportService).reportItem(any(), any(), any());
    }

    @Test
    @DisplayName("리뷰 신고 테스트")
    void reportReviewTest() throws Exception {
        // given
        ReportResponse reportResponse = new ReportResponse(
                1L,
                1L,
                null,
                1L,
                "신고 내용",
                false,
                false,
                LocalDateTime.now());

        given(reportService.reportReview(any(), any(), any())).willReturn(
                reportResponse
        );

        ReportRequest reportRequest = new ReportRequest("신고 내용");

        Gson gson = new Gson();
        String json = gson.toJson(reportRequest);

        String access = getToken();

        // when then
        mockMvc.perform(post("/api/report/reviews/1")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(customUserDetails)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.reviewId").value(1L))
                .andExpect(jsonPath("$.comments").value("신고 내용"))
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andDo(print());

        verify(reportService).reportReview(any(), any(), any());
    }

    @Test
    @DisplayName("아이템 신고 조회 테스트")
    void getItemReportTest() throws Exception {
        // given
        ReportResponse reportResponse = new ReportResponse(
                1L,
                1L,
                1L,
                null,
                "신고 내용",
                false,
                false,
                LocalDateTime.now());

        given(reportSearchService.getItemReport(any())).willReturn(
                reportResponse
        );

        // when then
        mockMvc.perform(get("/api/report/items/1")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.itemId").value(1L))
                .andExpect(jsonPath("$.comments").value("신고 내용"))
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andDo(print());

        verify(reportSearchService).getItemReport(any());
    }

    @Test
    @DisplayName("리뷰 신고 조회 테스트")
    void getReviewReportTest() throws Exception {
        // given
        ReportResponse reportResponse = new ReportResponse(
                1L,
                1L,
                null,
                1L,
                "신고 내용",
                false,
                false,
                LocalDateTime.now());

        given(reportSearchService.getReviewReport(any())).willReturn(
                reportResponse
        );

        // when then
        mockMvc.perform(get("/api/report/reviews/1")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.reviewId").value(1L))
                .andExpect(jsonPath("$.comments").value("신고 내용"))
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.accepted").value(false))
                .andExpect(jsonPath("$.createdDate").isNotEmpty())
                .andDo(print());

        verify(reportSearchService).getReviewReport(any());
    }

    @Test
    @DisplayName("아이템 신고 목록 조회 테스트")
    void getItemReportsTest() throws Exception {
        // given
        ReportResponse reportResponse1 = new ReportResponse(
                1L,
                1L,
                1L,
                null,
                "신고 내용",
                false,
                false,
                LocalDateTime.now());
        ReportResponse reportResponse2 = new ReportResponse(
                2L,
                2L,
                2L,
                null,
                "신고 내용2",
                false,
                false,
                LocalDateTime.now());

        List<ReportResponse> list = List.of(reportResponse1, reportResponse2);

        given(reportSearchService.getItemReports()).willReturn(
                new ApiResponse<>(list.size(), list)
        );

        // when then
        mockMvc.perform(get("/api/report/items")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].reportId").value(1L))
                .andExpect(jsonPath("$.data[0].userId").value(1L))
                .andExpect(jsonPath("$.data[0].itemId").value(1L))
                .andExpect(jsonPath("$.data[0].comments").value("신고 내용"))
                .andExpect(jsonPath("$.data[0].deleted").value(false))
                .andExpect(jsonPath("$.data[0].accepted").value(false))
                .andExpect(jsonPath("$.data[0].createdDate").isNotEmpty())
                .andExpect(jsonPath("$.data[1].reportId").value(2L))
                .andExpect(jsonPath("$.data[1].userId").value(2L))
                .andExpect(jsonPath("$.data[1].itemId").value(2L))
                .andExpect(jsonPath("$.data[1].comments").value("신고 내용2"))
                .andExpect(jsonPath("$.data[1].deleted").value(false))
                .andExpect(jsonPath("$.data[1].accepted").value(false))
                .andExpect(jsonPath("$.data[1].createdDate").isNotEmpty())
                .andDo(print());

        verify(reportSearchService).getItemReports();
    }

    @Test
    @DisplayName("리뷰 신고 목록 조회 테스트")
    void getReviewReportsTest() throws Exception {
        // given
        ReportResponse reportResponse1 = new ReportResponse(
                1L,
                1L,
                null,
                1L,
                "신고 내용",
                false,
                false,
                LocalDateTime.now());
        ReportResponse reportResponse2 = new ReportResponse(
                2L,
                2L,
                null,
                2L,
                "신고 내용2",
                false,
                false,
                LocalDateTime.now());

        List<ReportResponse> list = List.of(reportResponse1, reportResponse2);

        given(reportSearchService.getReviewReports()).willReturn(
                new ApiResponse<>(list.size(), list)
        );

        // when then
        mockMvc.perform(get("/api/report/reviews")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].reportId").value(1L))
                .andExpect(jsonPath("$.data[0].userId").value(1L))
                .andExpect(jsonPath("$.data[0].reviewId").value(1L))
                .andExpect(jsonPath("$.data[0].comments").value("신고 내용"))
                .andExpect(jsonPath("$.data[0].deleted").value(false))
                .andExpect(jsonPath("$.data[0].accepted").value(false))
                .andExpect(jsonPath("$.data[0].createdDate").isNotEmpty())
                .andExpect(jsonPath("$.data[1].reportId").value(2L))
                .andExpect(jsonPath("$.data[1].userId").value(2L))
                .andExpect(jsonPath("$.data[1].reviewId").value(2L))
                .andExpect(jsonPath("$.data[1].comments").value("신고 내용2"))
                .andExpect(jsonPath("$.data[1].deleted").value(false))
                .andExpect(jsonPath("$.data[1].accepted").value(false))
                .andExpect(jsonPath("$.data[1].createdDate").isNotEmpty())
                .andDo(print());

        verify(reportSearchService).getReviewReports();
    }

    @Test
    @DisplayName("신고 승인 테스트")
    void acceptReportTest() throws Exception {
        // given
        String access = getToken();

        // when then
        mockMvc.perform(patch("/api/report/1/accept")
                        .header("access", access)
                        .with(user(customUserDetails)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("신고 삭제 테스트")
    void deleteReportTest() throws Exception {
        // given
        String access = getToken();

        // when then
        mockMvc.perform(delete("/api/report/1")
                        .header("access", access)
                        .with(user(customUserDetails)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    private String getToken() {
        return jwtUtil.createJwt(100L, "loginId", "ROLE_SELLER", 60 * 10 * 1000L, null);
    }
}
package com.gracefullyugly.domain.report.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.item.entity.Item;
import com.gracefullyugly.domain.item.enumtype.Category;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.report.dto.ReportRequest;
import com.gracefullyugly.domain.report.dto.ReportResponse;
import com.gracefullyugly.domain.review.entity.Review;
import com.gracefullyugly.domain.review.repository.ReviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
class ReportSearchServiceTest {

    @Autowired
    ReportSearchService reportSearchService;

    @Autowired
    ReportService reportService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("상품 신고 조회 테스트")
    void getItemReportTest() {
        Item item = Item.builder()
                .userId(1L)
                .name("item")
                .productionPlace("한국")
                .categoryId(Category.FRUIT)
                .closedDate(LocalDateTime.now())
                .minUnitWeight(1)
                .price(1000)
                .totalSalesUnit(10)
                .minGroupBuyWeight(1)
                .description("description")
                .build();

        Item savedItem = itemRepository.save(item);

        ReportResponse reportResponse = reportService.reportItem(1L, savedItem.getId(), new ReportRequest("신고 내용"));

        ReportResponse getResponse = reportSearchService.getItemReport(savedItem.getId());

        assertEquals(reportResponse, getResponse);
    }

    @Test
    @DisplayName("리뷰 신고 조회 테스트")
    void getReviewReportTest() {
        Review review = Review.builder()
                .userId(1L)
                .itemId(1L)
                .comments("리뷰")
                .starPoint(4.0f)
                .build();

        Review savedReview = reviewRepository.save(review);

        ReportResponse reportResponse = reportService.reportReview(1L, savedReview.getId(), new ReportRequest("신고 내용"));

        ReportResponse getResponse = reportSearchService.getReviewReport(savedReview.getId());

        assertEquals(reportResponse, getResponse);
    }

    @Test
    @DisplayName("상품 신고 목록 조회 테스트")
    void getItemReportsTest() {
        Item item = Item.builder()
                .userId(1L)
                .name("item")
                .productionPlace("한국")
                .categoryId(Category.FRUIT)
                .closedDate(LocalDateTime.now())
                .minUnitWeight(1)
                .price(1000)
                .totalSalesUnit(10)
                .minGroupBuyWeight(1)
                .description("description")
                .build();

        Item savedItem = itemRepository.save(item);

        ReportResponse reportResponse1 = reportService.reportItem(1L, savedItem.getId(), new ReportRequest("신고 내용"));

        ReportResponse reportResponse2 = reportService.reportItem(2L, savedItem.getId(), new ReportRequest("신고 내용2"));

        ApiResponse<List<ReportResponse>> response = reportSearchService.getItemReports();

        assertEquals(response.getData().size(), 2);
        assertEquals(response.getData().get(0), reportResponse1);
        assertEquals(response.getData().get(1), reportResponse2);
    }

    @Test
    @DisplayName("리뷰 신고 목록 조회 테스트")
    void getReviewReportsTest() {
        Review review = Review.builder()
                .userId(1L)
                .itemId(1L)
                .comments("리뷰")
                .starPoint(4.0f)
                .build();

        Review savedReview = reviewRepository.save(review);

        ReportResponse reportResponse1 = reportService.reportReview(1L, savedReview.getId(),
                new ReportRequest("신고 내용"));

        ReportResponse reportResponse2 = reportService.reportReview(2L, savedReview.getId(),
                new ReportRequest("신고 내용2"));

        ApiResponse<List<ReportResponse>> response = reportSearchService.getReviewReports();

        assertEquals(response.getData().size(), 2);
        assertEquals(response.getData().get(0), reportResponse1);
        assertEquals(response.getData().get(1), reportResponse2);
    }

    @Test
    @DisplayName("(예외) 신고 상품 목록 조회 테스트)")
    void getItemReportsExceptionTest() {
        assertThatThrownBy(() -> reportSearchService.getItemReports())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("신고된 상품이 없습니다.");
    }

    @Test
    @DisplayName("(예외) 신고 리뷰 목록 조회 테스트)")
    void getReviewReportsExceptionTest() {
        assertThatThrownBy(() -> reportSearchService.getReviewReports())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("신고된 리뷰가 없습니다.");
    }
}
package com.gracefullyugly.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.entity.Review;
import com.gracefullyugly.domain.review.repository.ReviewRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ReviewSearchServiceTest {

    @Autowired
    private ReviewSearchService reviewSearchService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰 아이디로 조회 테스트")
    void getReviewTest() {
        Review review = Review.builder()
                .userId(1L)
                .itemId(1L)
                .comments("좋아요")
                .starPoint(5)
                .build();

        Review savedReview = reviewRepository.save(review);

        ReviewResponse response = reviewSearchService.getReviewById(savedReview.getId());

        assertThat(response.getComments()).isEqualTo(review.getComments());
        assertThat(response.getStarPoint()).isEqualTo(review.getStarPoint());
    }

    @Test
    @DisplayName("상품 아이디로 리뷰 리스트 조회 테스트")
    void getReviewsByItemIdTest() {
        Review review1 = Review.builder()
                .userId(1L)
                .itemId(1L)
                .comments("좋아요")
                .starPoint(5)
                .build();

        Review review2 = Review.builder()
                .userId(2L)
                .itemId(1L)
                .comments("싫어요")
                .starPoint(1)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        ApiResponse<List<ReviewResponse>> reviewsByItemId = reviewSearchService.getReviewsByItemId(1L);

        Assertions.assertThat(reviewsByItemId.getData().size()).isEqualTo(2);
        Assertions.assertThat(reviewsByItemId.getData().get(0).getComments()).isEqualTo(review1.getComments());
        Assertions.assertThat(reviewsByItemId.getData().get(1).getComments()).isEqualTo(review2.getComments());
        Assertions.assertThat(reviewsByItemId.getData().get(0).getStarPoint()).isEqualTo(review1.getStarPoint());
        Assertions.assertThat(reviewsByItemId.getData().get(1).getStarPoint()).isEqualTo(review2.getStarPoint());
    }

    @Test
    @DisplayName("(실패) 상품 아이디로 리뷰 리스트 조회 테스트")
    void getReviewsByItemIdFailTest() {
        Review review1 = Review.builder()
                .userId(1L)
                .itemId(1L)
                .comments("좋아요")
                .starPoint(5)
                .build();

        Review review2 = Review.builder()
                .userId(2L)
                .itemId(1L)
                .comments("싫어요")
                .starPoint(1)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        Assertions.assertThatThrownBy(() -> reviewSearchService.getReviewsByItemId(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 상품에 대한 리뷰가 존재하지 않습니다.");
    }

}
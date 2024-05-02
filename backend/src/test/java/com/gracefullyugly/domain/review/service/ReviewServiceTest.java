package com.gracefullyugly.domain.review.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gracefullyugly.domain.review.dto.ReviewDto;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.entity.Review;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@Slf4j
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewSearchService reviewSearchService;

    @Test
    @DisplayName("리뷰 생성 테스트")
    void createReviewTest() {
        // given
        Long userId = 1L;
        Long itemId = 1L;
        ReviewDto reviewDto = new ReviewDto("좋아요", 5);

        // when
        ReviewResponse response = reviewService.createReview(userId, reviewDto, itemId);

        // then
        assertThat(response.getComments()).isEqualTo("좋아요");
        assertThat(response.getStarPoint()).isEqualTo(5);
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    void updateReviewTest() {
        // given
        Long userId = 1L;
        Long itemId = 1L;
        ReviewDto reviewDto = new ReviewDto("좋아요", 5);
        ReviewDto updateDto = new ReviewDto("싫어요", 1);

        // when
        ReviewResponse saveResponse = reviewService.createReview(userId, reviewDto, itemId);
        ReviewDto updateResponse = reviewService.updateReview(userId, saveResponse.getReviewId(), updateDto);

        // then
        assertThat(updateResponse.getComments()).isEqualTo("싫어요");
        assertThat(updateResponse.getStarPoint()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 수정 실패 테스트")
    void updateReviewFailTest() {
        // given
        Long userId = 1L;
        Long itemId = 1L;
        ReviewDto reviewDto = new ReviewDto("좋아요", 5);
        ReviewDto updateDto = new ReviewDto("싫어요", 1);

        // when
        ReviewResponse saveResponse = reviewService.createReview(userId, reviewDto, itemId);

        // then
        assertThatThrownBy(() -> reviewService.updateReview(2L, saveResponse.getReviewId(), updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 리뷰만 수정할 수 있습니다.");
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteReviewTest() {
        // given
        Long userId = 1L;
        Long itemId = 1L;
        ReviewDto reviewDto = new ReviewDto("좋아요", 5);

        // when
        ReviewResponse response = reviewService.createReview(userId, reviewDto, itemId);
        reviewService.deleteReview(userId, response.getReviewId());

        // then
        Review findReview = reviewSearchService.findById(response.getReviewId());
        Assertions.assertThat(findReview.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("리뷰 삭제 실패 테스트")
    void deleteReviewFailTest() {
        // given
        Long userId = 1L;
        Long itemId = 1L;
        ReviewDto reviewDto = new ReviewDto("좋아요", 5);

        // when
        ReviewResponse response = reviewService.createReview(userId, reviewDto, itemId);
        Long reviewId = response.getReviewId();

        // then
        assertThatThrownBy(() -> reviewService.deleteReview(2L, reviewId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본인의 리뷰만 삭제할 수 있습니다.");
    }
}
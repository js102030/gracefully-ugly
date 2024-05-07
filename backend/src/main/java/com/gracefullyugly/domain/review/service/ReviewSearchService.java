package com.gracefullyugly.domain.review.service;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.review.dto.ReviewDtoUtil;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.entity.Review;
import com.gracefullyugly.domain.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewSearchService {

    private final ReviewRepository reviewRepository;

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다."));
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Review findReview = findById(reviewId);

        return ReviewDtoUtil.reviewToReviewResponse(findReview);
    }

    public int countByUserId(Long userId) {
        return reviewRepository.countByUserId(userId);
    }

    public ApiResponse<List<ReviewResponse>> getReviewsByItemId(Long itemId) {
        List<Review> reviews = reviewRepository.findByItemId(itemId);

        if (reviews.isEmpty()) {
            throw new IllegalArgumentException("해당 상품에 대한 리뷰가 존재하지 않습니다.");
        }

        List<ReviewResponse> reviewResponses = reviews
                .stream()
                .map(ReviewDtoUtil::reviewToReviewResponse)
                .toList();

        return new ApiResponse<>(reviewResponses.size(), reviewResponses);
    }
}

package com.gracefullyugly.domain.review.service;

import com.gracefullyugly.domain.review.dto.ReviewDto;
import com.gracefullyugly.domain.review.dto.ReviewDtoUtil;
import com.gracefullyugly.domain.review.dto.ReviewResponse;
import com.gracefullyugly.domain.review.entity.Review;
import com.gracefullyugly.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewSearchService reviewSearchService;

    public ReviewResponse createReview(Long userId, ReviewDto request, Long itemId) {
        Review review = Review.builder()
                .userId(userId)
                .itemId(itemId)
                .comments(request.getComments())
                .starPoint(request.getStarPoint())
                .build();

        Review savedReview = reviewRepository.save(review);

        return ReviewDtoUtil.reviewToReviewResponse(savedReview);
    }

    public ReviewDto updateReview(Long userId, Long reviewId, ReviewDto request) {
        Review findReview = reviewSearchService.findById(reviewId);

        validaUser(findReview, userId, "본인의 리뷰만 수정할 수 있습니다.");

        return findReview.updateReview(request);
    }

    public void deleteReview(Long userId, Long reviewId) {
        Review findReview = reviewSearchService.findById(reviewId);

        validaUser(findReview, userId, "본인의 리뷰만 삭제할 수 있습니다.");

        findReview.deleteReview();
    }

    private void validaUser(Review findReview, Long userId, String s) {
        if (!findReview.getUserId().equals(userId)) {
            throw new IllegalArgumentException(s);
        }
    }
}

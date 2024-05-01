package com.gracefullyugly.domain.review.dto;

import com.gracefullyugly.domain.review.entity.Review;

public class ReviewDtoUtil {

    public static ReviewResponse reviewToReviewResponse(Review review) {

        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .itemId(review.getItemId())
                .contents(review.getComments())
                .starPoint(review.getStarPoint())
                .isDeleted(review.isDeleted())
                .build();

    }
}

package com.gracefullyugly.domain.review.dto;

public interface ReviewWithImageResponse {

    Long getReviewId();

    Long getUserId();

    Long getItemId();

    String getUserNickname();

    String getComments();

    float getStarPoint();

    String getImageUrl();
}

package com.gracefullyugly.domain.review.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import com.gracefullyugly.domain.review.dto.ReviewDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private Long userId;

    private Long itemId;

    private String comments;

    private float starPoint;

    private boolean isDeleted;

    @Builder
    public Review(Long userId, Long itemId, String comments, float starPoint) {
        this.userId = userId;
        this.itemId = itemId;
        this.comments = comments;
        this.starPoint = starPoint;
    }

    public ReviewDto updateReview(ReviewDto reviewRequest) {
        this.comments = reviewRequest.getComments();
        this.starPoint = reviewRequest.getStarPoint();

        return ReviewDto.builder()
                .comments(this.comments)
                .starPoint(this.starPoint)
                .build();
    }

    public void deleteReview() {
        this.isDeleted = true;
    }

}

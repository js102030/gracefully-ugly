package com.gracefullyugly.domain.report.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    private Long userId;

    private Long itemId;

    private Long reviewId;

    private String comments;

    private boolean isAccepted;

    private boolean isDeleted;

    private Report(Long userId, Long itemId, Long reviewId, String comments) {
        this.userId = userId;
        this.itemId = itemId;
        this.reviewId = reviewId;
        this.comments = comments;
    }

    public static Report forItem(Long userId, Long itemId, String comments) {
        return new Report(userId, itemId, null, comments);
    }

    public static Report forReview(Long userId, Long reviewId, String comments) {
        return new Report(userId, null, reviewId, comments);
    }

    public void accept() {
        this.isAccepted = true;
    }

    public void delete() {
        this.isDeleted = true;
    }
}

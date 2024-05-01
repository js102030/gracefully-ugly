package com.gracefullyugly.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long userId;
    private Long itemId;
    private String contents;
    private float starPoint;
    private boolean isDeleted;

}

package com.gracefullyugly.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@Builder
public class ReviewDto {

    @NotBlank(message = "리뷰는 필수입니다.")
    private String comments;

    @Range(min = 1, max = 5)
    private float starPoint;

}

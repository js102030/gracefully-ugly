package com.gracefullyugly.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDescriptionRequest {

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

}

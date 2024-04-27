package com.gracefullyugly.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BasicRegResponse {

    private Long userId;
    private String loginId;

}

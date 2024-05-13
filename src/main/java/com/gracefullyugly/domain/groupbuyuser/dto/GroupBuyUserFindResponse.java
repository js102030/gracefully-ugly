package com.gracefullyugly.domain.groupbuyuser.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupBuyUserFindResponse {

    private LocalDateTime joinDate;
    private int quantity;
}

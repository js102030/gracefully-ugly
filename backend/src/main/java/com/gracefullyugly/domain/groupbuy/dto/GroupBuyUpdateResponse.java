package com.gracefullyugly.domain.groupbuy.dto;

import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupBuyUpdateResponse {

    private Long groupBuyId;
    private GroupBuyStatus status;
}

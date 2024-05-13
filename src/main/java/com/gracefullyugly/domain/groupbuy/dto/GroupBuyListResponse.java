package com.gracefullyugly.domain.groupbuy.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupBuyListResponse {

    List<GroupBuySelectDto> groupBuyList;
}

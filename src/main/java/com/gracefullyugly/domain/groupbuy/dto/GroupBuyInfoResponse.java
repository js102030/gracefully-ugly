package com.gracefullyugly.domain.groupbuy.dto;

import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;

public interface GroupBuyInfoResponse {

    Long getGroupBuyId();

    Long getItemId();

    String getItemName();

    GroupBuyStatus getGroupBuyStatus();

    Long getParticipantCount();
}

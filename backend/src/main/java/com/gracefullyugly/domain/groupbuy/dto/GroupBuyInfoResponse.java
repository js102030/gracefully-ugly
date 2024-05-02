package com.gracefullyugly.domain.groupbuy.dto;

import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import java.time.LocalDateTime;

public interface GroupBuyInfoResponse {

    Long getGroupBuyId();

    Long getItemId();

    String getItemName();

    GroupBuyStatus getGroupBuyStatus();

    LocalDateTime getEndDate();

    Long getParticipantCount();
}

package com.gracefullyugly.domain.groupbuy.dto;

import com.gracefullyugly.domain.groupbuy.enumtype.GroupBuyStatus;
import java.time.LocalDateTime;

public interface GroupBuySelectDto {

    Long getGroupBuyId();

    Long getItemId();

    GroupBuyStatus getGroupBuyStatus();

    LocalDateTime getEndDate();

    Long getParticipantCount();
}

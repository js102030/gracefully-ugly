package com.gracefullyugly.domain.user.dto;

import java.time.LocalDateTime;

public interface SellerDetailsResponse {

    String getUserNickname();

    Integer getTotalPrice();

    Boolean getIsPaid();

    Boolean getIsRefunded();

    String getAddress();

    LocalDateTime getOrderDate();

}

package com.gracefullyugly.domain.item.dto;

import com.gracefullyugly.domain.item.enumtype.Category;
import java.time.LocalDateTime;

public interface ItemWithImageUrlResponse {

    Long getId();

    Long getUserId();

    String getName();

    String getProductionPlace();

    Category categoryId();

    LocalDateTime getClosedDate();

    LocalDateTime getCreatedDate();

    LocalDateTime getLastModifiedDate();

    int getMinUnitWeight();

    int getPrice();

    int getTotalSalesUnit();

    int getMinGroupBuyWeight();

    String getDescription();

    String getImageUrl();

}

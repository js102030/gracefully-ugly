package com.gracefullyugly.domain.cart.dto;


import com.gracefullyugly.domain.item.enumtype.Category;
import java.sql.Timestamp;

public interface CartListResponse {

    Long getCartItemId();

    Long getItemCount();

    Long getItemId();

    String getName();

    int getPrice();

    Category getCategoryId();

    Timestamp getCloseDate();

    String getImageUrl();
}

package com.gracefullyugly.domain.item.enumtype;

import lombok.Getter;

@Getter
public enum Category {
    VEGETABLE("VEGETABLE"),
    FRUIT("FRUIT"),
    OTHER("OTHER");

    private final String value;

    Category(String value) {
        this.value = value;
    }

}

package com.gracefullyugly.domain.image.entity;

import com.gracefullyugly.common.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private Long itemId;

    private Long reviewId;

    private String url;

    private String originalImageName;

    private String storedImageName;

    private long size;

    private boolean isDeleted;

    public static Image createItemImage(Long itemId, String url, String originalImageName, String storedImageName,
                                        long size) {
        Image image = new Image();
        image.itemId = itemId;
        image.url = url;
        image.originalImageName = originalImageName;
        image.storedImageName = storedImageName;
        image.size = size;
        return image;
    }

    public static Image createReviewImage(Long reviewId, String url, String originalImageName, String storedImageName,
                                          long size) {
        Image image = new Image();
        image.reviewId = reviewId;
        image.url = url;
        image.originalImageName = originalImageName;
        image.storedImageName = storedImageName;
        image.size = size;
        return image;
    }
}

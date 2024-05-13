package com.gracefullyugly.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ImageUploadResponse {

    private String url;
    private String originalImageName;
    private String storedImageName;
    private long size;

}

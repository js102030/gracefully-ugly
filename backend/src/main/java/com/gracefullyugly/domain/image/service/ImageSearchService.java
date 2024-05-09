package com.gracefullyugly.domain.image.service;

import com.gracefullyugly.domain.image.entity.Image;
import com.gracefullyugly.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageSearchService {

    private final ImageRepository imageRepository;

    public String getItemImageUrl(Long itemId) {
        Image findImage = imageRepository.findByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("itemId : " + itemId + "에 해당하는 이미지가 없습니다."));

        return findImage.getUrl();
    }
}

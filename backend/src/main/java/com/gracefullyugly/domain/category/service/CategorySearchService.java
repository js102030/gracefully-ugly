package com.gracefullyugly.domain.category.service;

import com.gracefullyugly.domain.category.entity.Category;
import com.gracefullyugly.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategorySearchService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(categoryId + "에 해당하는 카테고리가 없습니다."));
    }
}

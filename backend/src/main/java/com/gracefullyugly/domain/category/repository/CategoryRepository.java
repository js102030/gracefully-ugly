package com.gracefullyugly.domain.category.repository;

import com.gracefullyugly.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

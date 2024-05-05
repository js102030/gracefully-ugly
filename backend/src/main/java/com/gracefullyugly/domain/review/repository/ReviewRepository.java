package com.gracefullyugly.domain.review.repository;

import com.gracefullyugly.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByItemId(Long itemId);

    int countByUserId(Long userId);

}

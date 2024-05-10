package com.gracefullyugly.domain.review.repository;

import com.gracefullyugly.domain.review.dto.ReviewWithImageResponse;
import com.gracefullyugly.domain.review.entity.Review;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByItemId(Long itemId);

    int countByUserId(Long userId);

    @Query("SELECT AVG(r.starPoint) FROM Review r WHERE r.itemId = :itemId AND r.isDeleted = false")
    Float findAverageStarPointsByItemId(@Param("itemId") Long itemId);

    @Query(value = "SELECT r.review_id AS reviewId, " +
            "r.user_id AS userId, " +
            "r.item_id AS itemId, " +
            "u.nickname AS userNickname, " +
            "r.comments AS comments, " +
            "r.star_point AS starPoint, " +
            "i.url AS imageUrl " +
            "FROM review r " +
            "LEFT JOIN image i ON r.review_id = i.review_id " +
            "LEFT JOIN users u ON r.user_id = u.user_id " +
            "WHERE r.item_id = :itemId AND r.is_deleted = false AND (i.is_deleted = false OR i.review_id IS NULL)",
            nativeQuery = true)
    List<ReviewWithImageResponse> getReviewsWithImagesByItemId(@Param("itemId") Long itemId);

}

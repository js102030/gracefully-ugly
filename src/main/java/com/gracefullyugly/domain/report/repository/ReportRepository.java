package com.gracefullyugly.domain.report.repository;

import com.gracefullyugly.domain.report.entity.Report;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByItemId(Long itemId);

    Optional<Report> findByReviewId(Long reviewId);

    List<Report> findByReviewIdIsNull();

    List<Report> findByItemIdIsNull();

    List<Report> findByIsAcceptedFalseAndIsDeletedFalse();

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.itemId = :itemId")
    void deleteCartItemByItemId(@Param("itemId") Long itemId);

}

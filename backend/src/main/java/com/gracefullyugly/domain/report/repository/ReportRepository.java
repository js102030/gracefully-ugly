package com.gracefullyugly.domain.report.repository;

import com.gracefullyugly.domain.report.entity.Report;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByItemId(Long itemId);

    Optional<Report> findByReviewId(Long reviewId);

    List<Report> findByReviewIdIsNull();

    List<Report> findByItemIdIsNull();

}

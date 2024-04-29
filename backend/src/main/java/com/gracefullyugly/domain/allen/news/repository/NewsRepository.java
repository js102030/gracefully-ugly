package com.gracefullyugly.domain.allen.news.repository;

import com.gracefullyugly.domain.allen.news.entity.News;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {

    void deleteAllByCreatedAtBefore(LocalDateTime expiryDate);
}

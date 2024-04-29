package com.gracefullyugly.domain.allen.news.scheduling;

import com.gracefullyugly.domain.allen.news.repository.NewsRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NewsCleanupTask {

    private final NewsRepository newsRepository;

    @Scheduled(cron = "0 0/30 * * * ?")  // 매 30분마다 실행
    public void deleteExpiredVerifications() {
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        newsRepository.deleteAllByCreatedAtBefore(thirtyMinutesAgo);
    }
}

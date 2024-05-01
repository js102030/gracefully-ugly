package com.gracefullyugly.domain.allen.news.scheduling;

import com.gracefullyugly.domain.allen.news.repository.NewsRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NewsCleanupTask {

    private final NewsRepository newsRepository;

    @Scheduled(cron = "0 0/30 * * * ?")  // 매 30분마다 실행
    @Transactional
    public void deleteExpiredVerifications() {
        // 2시간 전에 생성된 뉴스 삭제
        LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
        newsRepository.deleteAllByCreatedAtBefore(twoHoursAgo);
    }

}

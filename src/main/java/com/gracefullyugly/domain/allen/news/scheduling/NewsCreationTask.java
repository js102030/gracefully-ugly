package com.gracefullyugly.domain.allen.news.scheduling;

import com.gracefullyugly.domain.allen.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsCreationTask {

    private final NewsService newsService;

    @Transactional
    @Scheduled(initialDelay = 0, fixedRate = 3600000)  // 앱 시작 시와 매 1시간마다 실행
    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void createNews() {
        newsService.saveNewsList();
    }
}

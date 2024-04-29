package com.gracefullyugly.domain.allen.news.scheduling;

import com.gracefullyugly.domain.allen.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class NewsCreationTask {

    private final NewsService newsService;

    @Transactional
    @Scheduled(initialDelay = 0, fixedRate = 1800000)  // 앱 시작 시와 매 30분마다 실행
    public void createNews() {
        newsService.saveNewsList();
    }
}

package com.gracefullyugly.domain.allen.news.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.allen.news.entity.News;
import com.gracefullyugly.domain.allen.news.service.NewsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/news")
    public ResponseEntity<ApiResponse<List<News>>> saveFiveNews() {
        return ResponseEntity
                .status(CREATED)
                .body(newsService.saveNewsList());
    }

    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<News>>> getLastFiveNews() {
        return ResponseEntity
                .ok(newsService.getLastFiveNews());
    }

    @GetMapping("/news/{newsId}")
    public ResponseEntity<News> getNews(@PathVariable Long newsId) {
        final News news = newsService.getNews(newsId);
        return ResponseEntity
                .ok(news);
    }
}

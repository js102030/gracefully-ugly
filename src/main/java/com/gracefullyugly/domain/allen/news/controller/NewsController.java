package com.gracefullyugly.domain.allen.news.controller;

import static org.springframework.http.HttpStatus.CREATED;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.allen.news.entity.News;
import com.gracefullyugly.domain.allen.news.service.NewsService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "앨런 AI 뉴스")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/all")
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "뉴스 생성", description = "앨런 뉴스 생성하기")
    @PostMapping("/news")
    public ResponseEntity<ApiResponse<List<News>>> saveFiveNews() {
        return ResponseEntity
                .status(CREATED)
                .body(newsService.saveNewsList());
    }

    @Operation(summary = "뉴스 최근 5개 조회", description = "앨런 뉴스 최근 5개 조회하기")
    @GetMapping("/news")
    public ResponseEntity<ApiResponse<List<News>>> getLastFiveNews() {
        return ResponseEntity
                .ok(newsService.getLastFiveNews());
    }

    @Operation(summary = "뉴스 조회", description = "앨런 뉴스 조회하기")
    @GetMapping("/news/{newsId}")
    public ResponseEntity<News> getNews(@PathVariable Long newsId) {
        final News news = newsService.getNews(newsId);
        return ResponseEntity
                .ok(news);
    }
}

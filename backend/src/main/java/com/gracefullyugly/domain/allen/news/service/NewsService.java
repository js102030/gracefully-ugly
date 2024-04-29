package com.gracefullyugly.domain.allen.news.service;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.allen.news.entity.News;
import com.gracefullyugly.domain.allen.news.repository.NewsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {

    private static final String BASE_URL = "https://kdt-api-function.azurewebsites.net/api/v1/question";
    private static final String CLIENT_ID = "702270b5-e901-4823-a62e-4f8708c65509";
    private static final String REQUEST_CONTENT = "오늘의 농산물 뉴스 5개를 알려주세요.\n"
            + "각 뉴스들을 120자 내외로 요약해, 1. 2. 3. 4. 5. 로 구분하여 응답하세요.\n"
            + "5개의 뉴스 외에 다른 말은 하지 말아주세요.\n"
            + "마지막 문단에 출처를 쓰지 말고 출처는 각 뉴스 뒤에 이어붙여주세요.";

    private final NewsRepository newsRepository;

    public ApiResponse<List<News>> saveNewsList() {
        String response = fetchNewsResponse();

        String contentBody = getContentBody(response);

        List<News> content = listingNewses(contentBody);

        List<News> news = newsRepository.saveAll(content);

        return new ApiResponse<>(news.size(), news);
    }

    public ApiResponse<List<News>> getNewsList() {
        List<News> allNews = newsRepository.findAll();

        return new ApiResponse<>(allNews.size(), allNews);
    }

    private String fetchNewsResponse() {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("content", REQUEST_CONTENT)
                .queryParam("client_id", CLIENT_ID)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    private String getContentBody(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("content");
    }

    private List<News> listingNewses(String newsContent) {
        List<News> newsList = new ArrayList<>();
        String[] newsItems = newsContent.split("\n");

        for (String newsItem : newsItems) {
            newsList.add(formattedNewsItemToNews(newsItem));
        }

        return newsList;
    }

    private News formattedNewsItemToNews(String newsItem) {
        String cleanedNews = cleanNewsText(newsItem);

        return News.builder()
                .contents(formatNews(cleanedNews))
                .source(formatSource(cleanedNews))
                .build();
    }

    private String cleanNewsText(String newsItem) {
        return newsItem.replaceAll("\\d+\\.|\\(출처\\d+\\)", "").replaceAll("\\[\\]", "[(출처)]");
    }

    private String formatNews(String cleanedNews) {
        String[] parts = cleanedNews.split("\\[", 2);
        return (parts[0] + ".").trim();
    }

    private String formatSource(String cleanedNews) {
        String[] parts = cleanedNews.split("\\[", 2);
        if (parts.length > 1) {
            return "[" + parts[1].substring(0, parts[1].length() - 1);
        }
        return "Source not found.";
    }

}

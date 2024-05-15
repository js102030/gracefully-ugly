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
    private static final String CLIENT_ID = "d4ee2bfb-6870-43c1-b630-33e50ff05ca0";
    private static final String REQUEST_CONTENT =
            """
                    오늘의 농산물 뉴스 5개를 알려주세요.
                    각 뉴스들을 120자 내외로 요약해, 1. 2. 3. 4. 5. 로 구분하여 응답하세요.
                    5개의 뉴스 외에 다른 말은 하지 말아주세요.
                    마지막 문단에 출처를 쓰지 말고 출처는 각 뉴스 뒤에 이어붙여주세요.
                    """;

    private final NewsRepository newsRepository;

    public ApiResponse<List<News>> saveNewsList() {
        String response = fetchNewsResponse();

        String contentBody = getContentBody(response);

        List<News> news = listingNewses(contentBody);

        List<News> savedNews = newsRepository.saveAll(news);

        return new ApiResponse<>(savedNews.size(), savedNews);
    }

    public ApiResponse<List<News>> getLastFiveNews() {
        List<News> lastFiveNews = newsRepository.findTop5ByOrderByIdAsc();

        if (lastFiveNews.isEmpty()) {
            throw new IllegalArgumentException("뉴스가 존재하지 않습니다.");
        }

        return new ApiResponse<>(lastFiveNews.size(), lastFiveNews);
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
            News news = new News(newsItem);

            newsList.add((news));
        }

        return newsList;
    }

    public News getNews(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("해당 뉴스가 존재하지 않습니다. id: " + newsId));
    }
}

package com.gracefullyugly.domain.qna.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.dto.QuestionDto;
import com.gracefullyugly.domain.qna.service.QnASearchService;
import com.gracefullyugly.domain.qna.service.QnAService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class QnAControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QnAService qnAService;

    @Autowired
    private JWTUtil jwtUtil;

    @MockBean
    private QnASearchService qnASearchService;

    @Test
    @DisplayName("QnA 생성 테스트")
    void createQnATest() throws Exception {
        // given
        given(qnAService.createQnA(any(), any(), any()))
                .willReturn(
                        new QnADto(100L, 100L, 100L, "질문", "답변")
                );

        Gson gson = new Gson();
        String request = gson.toJson(new QuestionDto("질문"));

        String access = getToken();

        // when & then
        mockMvc.perform(post("/api/questions/1")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.question").value("질문"))
                .andDo(print());

        verify(qnAService).createQnA(any(), any(), any());
    }

    @Test
    @DisplayName("QnA 답변 생성 테스트")
    void createAnswerTest() throws Exception {
        // given
        given(qnAService.createAnswer(any(), any(), any()))
                .willReturn(
                        new QnADto(100L, 100L, 100L, "질문", "답변")
                );

        Gson gson = new Gson();
        String request = gson.toJson(new AnswerDto("답변"));

        String access = getToken();

        // when & then
        mockMvc.perform(post("/api/answers/1")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.answer").value("답변"))
                .andDo(print());

        verify(qnAService).createAnswer(any(), any(), any());
    }

    @Test
    @DisplayName("QnA 조회 테스트")
    void readQnATest() throws Exception {
        // given
        given(qnASearchService.getQnA(any()))
                .willReturn(new QnADto(100L, 100L, 100L, "질문", "답변"));

        String access = getToken();

        // when & then
        mockMvc.perform(get("/api/questions/1")
                        .header("access", access))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("질문"))
                .andExpect(jsonPath("$.answer").value("답변"))
                .andDo(print());

        verify(qnASearchService).getQnA(any());
    }

    @Test
    @DisplayName("QnA 목록 조회 테스트")
    void readQnAsTest() throws Exception {
        // given
        List<QnADto> list = List
                .of(
                        new QnADto(100L, 100L, 100L, "질문", "답변"),
                        new QnADto(101L, 101L, 101L, "질문2", "답변2")
                );
        given(qnASearchService.getQnAList(any()))
                .willReturn(new ApiResponse<>(list.size(), list));

        String access = getToken();

        // when & then
        mockMvc.perform(get("/api/questions/items/1")
                        .header("access", access))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].question").value("질문"))
                .andExpect(jsonPath("$.data[0].answer").value("답변"))
                .andExpect(jsonPath("$.data[1].question").value("질문2"))
                .andExpect(jsonPath("$.data[1].answer").value("답변2"))
                .andDo(print());

        verify(qnASearchService).getQnAList(any());
    }

    private String getToken() {
        return jwtUtil.createJwt("access", 100L, "loginId", "ROLE_BUYER", 60 * 10 * 1000L);
    }


}
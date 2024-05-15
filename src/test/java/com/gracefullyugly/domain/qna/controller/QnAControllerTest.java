package com.gracefullyugly.domain.qna.controller;

import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.gracefullyugly.common.security.CustomUserDetails;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.domain.item.dto.ItemRequest;
import com.gracefullyugly.domain.item.repository.ItemRepository;
import com.gracefullyugly.domain.item.service.ItemService;
import com.gracefullyugly.domain.qna.dto.AnswerDto;
import com.gracefullyugly.domain.qna.dto.QnADto;
import com.gracefullyugly.domain.qna.dto.QuestionDto;
import com.gracefullyugly.domain.qna.service.QnASearchService;
import com.gracefullyugly.domain.qna.service.QnAService;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testuserdetails.TestUserDetailsService;
import com.gracefullyugly.testutil.SetupDataUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    private TestUserDetailsService testUserDetailsService;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setupTestData() {
        // 회원 정보 세팅
        userRepository.save(SetupDataUtils.makeTestSellerUser((passwordEncoder)));

        // 상품 정보 세팅
        List<ItemRequest> testItemData = SetupDataUtils.makeTestItemRequest();

        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(0));
        itemService.save(userRepository.findByNickname(TEST_NICKNAME).get().getId(), testItemData.get(1));

        // UserDetails 세팅
        testUserDetailsService = new TestUserDetailsService(userRepository);
        customUserDetails = (CustomUserDetails) testUserDetailsService.loadUserByUsername(TEST_LOGIN_ID);
    }

    @AfterEach
    void deleteTestData() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("QnA 생성 테스트")
    void createQnATest() throws Exception {
        // given
        given(qnAService.createQnA(any(), any(), any()))
                .willReturn(
                        new QnADto(100L, 100L, "xxx", 100L, "질문", "답변")
                );

        Gson gson = new Gson();
        String request = gson.toJson(new QuestionDto("질문"));

        String access = getToken();

        // when & then
        mockMvc.perform(post("/api/questions/1")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .with(user(customUserDetails)))
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
                        new QnADto(100L, 100L, "xxx", 100L, "질문", "답변")
                );

        Gson gson = new Gson();
        String request = gson.toJson(new AnswerDto("답변"));

        String access = getToken();

        // when & then
        mockMvc.perform(post("/api/answers/1")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                        .with(user(customUserDetails)))
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
                .willReturn(new QnADto(100L, 100L, "xxx", 100L, "질문", "답변"));

        String access = getToken();

        // when & then
        mockMvc.perform(get("/api/questions/1")
                        .header("access", access)
                        .with(user(customUserDetails)))
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
                        new QnADto(100L, 100L, "xxx", 100L, "질문", "답변"),
                        new QnADto(101L, 101L, "xxx2", 101L, "질문2", "답변2")
                );
        given(qnASearchService.getQnAList(any()))
                .willReturn(
                        list
                );

        String access = getToken();

        // when & then
        mockMvc.perform(get("/api/questions/items/1")
                        .header("access", access)
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value("질문"))
                .andExpect(jsonPath("$[0].answer").value("답변"))
                .andExpect(jsonPath("$[1].question").value("질문2"))
                .andExpect(jsonPath("$[1].answer").value("답변2"))
                .andDo(print());

        verify(qnASearchService).getQnAList(any());
    }

    private String getToken() {
        return jwtUtil.createJwt(100L, "loginId", "ROLE_SELLER", 60 * 10 * 1000L, null);
    }


}
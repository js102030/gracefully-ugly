package com.gracefullyugly.domain.user.controller.api;

import static com.gracefullyugly.testutil.SetupDataUtils.NEW_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADDRESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_EMAIL;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_PASSWORD;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ROLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.gracefullyugly.common.security.jwt.JWTUtil;
import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegResponse;
import com.gracefullyugly.domain.user.dto.FinalRegResponse;
import com.gracefullyugly.domain.user.dto.ProfileResponse;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import com.gracefullyugly.domain.user.dto.UpdateNicknameDto;
import com.gracefullyugly.domain.user.dto.UpdatePasswordRequest;
import com.gracefullyugly.domain.user.dto.UserResponse;
import com.gracefullyugly.domain.user.dto.ValidEmail;
import com.gracefullyugly.domain.user.dto.ValidLoginId;
import com.gracefullyugly.domain.user.dto.ValidNickname;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import com.gracefullyugly.domain.user.service.UserSearchService;
import com.gracefullyugly.domain.user.service.UserService;
import java.time.LocalDateTime;
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
class UserControllerTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserSearchService userSearchService;

    @Test
    @DisplayName("회원 가입 테스트")
    void createBasicAccountTest() throws Exception {
        // Given
        given(userService.createBasicAccount(any(BasicRegRequest.class)))
                .willReturn(new BasicRegResponse(100L, TEST_LOGIN_ID));

        Gson gson = new Gson();
        String json = gson.toJson(new BasicRegRequest(TEST_LOGIN_ID, TEST_PASSWORD));

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(100L))
                .andExpect(jsonPath("$.loginId").value(TEST_LOGIN_ID))
                .andDo(print());

        verify(userService).createBasicAccount(any(BasicRegRequest.class));
    }

    @Test
    @DisplayName("회원 가입 완료 테스트")
    void completeRegistrationTest() throws Exception {
        // Given
        AdditionalRegRequest additionalRegRequest = AdditionalRegRequest.builder()
                .role(TEST_ROLE)
                .nickname(TEST_NICKNAME)
                .address(TEST_ADDRESS)
                .build();

        given(userService.completeRegistration(100L, additionalRegRequest))
                .willReturn(FinalRegResponse.builder()
                        .userId(100L)
                        .loginId(TEST_LOGIN_ID)
                        .nickname(TEST_NICKNAME)
                        .address(TEST_ADDRESS)
                        .role(TEST_ROLE)
                        .createdDate(LocalDateTime.now())
                        .build()
                );

        Gson gson = new Gson();
        String json = gson.toJson(additionalRegRequest);

        String access = getToken();

        // When & Then
        mockMvc.perform(patch("/api/users/registration")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(100L))
                .andExpect(jsonPath("$.loginId").value(TEST_LOGIN_ID))
                .andExpect(jsonPath("$.nickname").value(TEST_NICKNAME))
                .andExpect(jsonPath("$.address").value(TEST_ADDRESS))
                .andExpect(jsonPath("$.role").value(TEST_ROLE.name()))
                .andDo(print());

        verify(userService).completeRegistration(any(Long.class), any(AdditionalRegRequest.class));
    }

    @Test
    @DisplayName("유저 조회 테스트")
    void getUserTest() throws Exception {
        // Given
        given(userSearchService.getUser(100L))
                .willReturn(
                        UserResponse.builder()
                                .userId(100L)
                                .signUpType(SignUpType.GENERAL)
                                .role(TEST_ROLE)
                                .loginId(TEST_LOGIN_ID)
                                .nickname(TEST_NICKNAME)
                                .email(TEST_EMAIL)
                                .address(TEST_ADDRESS)
                                .isBanned(false)
                                .isDeleted(false)
                                .isVerified(false)
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build()
                );

        // When & Then
        mockMvc.perform(get("/api/users/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(100L))
                .andExpect(jsonPath("$.signUpType").value(SignUpType.GENERAL.name()))
                .andExpect(jsonPath("$.role").value(TEST_ROLE.name()))
                .andExpect(jsonPath("$.loginId").value(TEST_LOGIN_ID))
                .andExpect(jsonPath("$.nickname").value(TEST_NICKNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.address").value(TEST_ADDRESS))
                .andExpect(jsonPath("$.banned").value(false))
                .andExpect(jsonPath("$.deleted").value(false))
                .andExpect(jsonPath("$.verified").value(false))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
                .andDo(print());

        verify(userSearchService).getUser(any(Long.class));
    }

    @Test
    @DisplayName("프로필 조회 테스트")
    void getProfileTest() throws Exception {
        // Given
        given(userSearchService.getProfile(any()))
                .willReturn(
                        ProfileResponse.builder()
                                .userId(100L)
                                .loginId(TEST_LOGIN_ID)
                                .nickname(TEST_NICKNAME)
                                .address(TEST_ADDRESS)
                                .email(TEST_EMAIL)
                                .role(TEST_ROLE)
                                .isVerified(false)
                                .buyCount(0)
                                .reviewCount(0)
                                .build()
                );

        // When & Then
        mockMvc.perform(get("/api/users/100/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(100L))
                .andExpect(jsonPath("$.loginId").value(TEST_LOGIN_ID))
                .andExpect(jsonPath("$.nickname").value(TEST_NICKNAME))
                .andExpect(jsonPath("$.address").value(TEST_ADDRESS))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.role").value(TEST_ROLE.name()))
                .andExpect(jsonPath("$.verified").value(false))
                .andExpect(jsonPath("$.buyCount").value(0))
                .andExpect(jsonPath("$.reviewCount").value(0))
                .andDo(print());

        verify(userSearchService).getProfile(any(Long.class));
    }

    @Test
    @DisplayName("닉네임 변경 테스트")
    void updateNicknameTest() throws Exception {
        // Given
        given(userService.updateNickname(100L, NEW_NICKNAME))
                .willReturn(
                        UserResponse.builder()
                                .userId(100L)
                                .signUpType(SignUpType.GENERAL)
                                .role(TEST_ROLE)
                                .loginId(TEST_LOGIN_ID)
                                .nickname(NEW_NICKNAME)
                                .email(TEST_EMAIL)
                                .address(TEST_ADDRESS)
                                .isBanned(false)
                                .isDeleted(false)
                                .isVerified(false)
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build()
                );

        Gson gson = new Gson();
        String json = gson.toJson(new UpdateNicknameDto(NEW_NICKNAME));

        String access = getToken();

        // When & Then
        mockMvc.perform(patch("/api/users/nickname")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(NEW_NICKNAME))
                .andDo(print());

        verify(userService).updateNickname(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void updatePasswordTest() throws Exception {
        //TODO 비밀번호 암호화 테스트 추가하여 테스트 해야 하는데 방법을 모름...

        // Given
        String newPassword = "newPassword";

        Gson gson = new Gson();
        String json = gson.toJson(new UpdatePasswordRequest(newPassword));

        String access = getToken();
        // When
        mockMvc.perform(patch("/api/users/password")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        // Then
        verify(userService).updatePassword(100L, newPassword);
    }

    @Test
    @DisplayName("주소 변경 테스트")
    void updateAddressTest() throws Exception {
        // Given
        given(userService.updateAddress(100L, TEST_ADDRESS))
                .willReturn(
                        UserResponse.builder()
                                .userId(100L)
                                .signUpType(SignUpType.GENERAL)
                                .role(TEST_ROLE)
                                .loginId(TEST_LOGIN_ID)
                                .nickname(TEST_NICKNAME)
                                .email(TEST_EMAIL)
                                .address(TEST_ADDRESS)
                                .isBanned(false)
                                .isDeleted(false)
                                .isVerified(false)
                                .createdDate(LocalDateTime.now())
                                .lastModifiedDate(LocalDateTime.now())
                                .build()
                );

        Gson gson = new Gson();
        String json = gson.toJson(new UpdateAddressDto(TEST_ADDRESS));

        String access = getToken();

        // When & Then
        mockMvc.perform(patch("/api/users/address")
                        .header("access", access)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value(TEST_ADDRESS))
                .andDo(print());

        verify(userService).updateAddress(any(Long.class), any(String.class));
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void deleteUserTest() throws Exception {
        // Given
        doNothing().when(userService).delete(100L);

        String access = getToken();

        // When
        mockMvc.perform(delete("/api/users")
                        .header("access", access))
                .andExpect(status().isNoContent())
                .andDo(print());

        // Then
        verify(userService).delete(100L);
    }

    @Test
    @DisplayName("로그인 아이디 중복 확인 테스트")
    void checkLoginIdAvailabilityTest() throws Exception {
        // Given
        given(userSearchService.existsByLoginId(TEST_LOGIN_ID))
                .willReturn(true);

        Gson gson = new Gson();
        String json = gson.toJson(new ValidLoginId(TEST_LOGIN_ID));

        // When & Then
        mockMvc.perform(get("/api/loginId-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());

        verify(userSearchService).existsByLoginId(TEST_LOGIN_ID);
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    void checkNicknameAvailabilityTest() throws Exception {
        // Given
        given(userSearchService.existsByNickName(TEST_NICKNAME))
                .willReturn(true);

        Gson gson = new Gson();
        String json = gson.toJson(new ValidNickname(TEST_NICKNAME));

        // When & Then
        mockMvc.perform(get("/api/nickname-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());

        verify(userSearchService).existsByNickName(TEST_NICKNAME);
    }

    @Test
    @DisplayName("이메일 중복 확인 테스트")
    void checkEmailAvailabilityTest() throws Exception {
        // Given
        given(userSearchService.existsByEmail(TEST_EMAIL))
                .willReturn(true);

        Gson gson = new Gson();
        String json = gson.toJson(new ValidEmail(TEST_EMAIL));

        // When & Then
        mockMvc.perform(get("/api/email-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());

        verify(userSearchService).existsByEmail(TEST_EMAIL);
    }

    private String getToken() {
        return jwtUtil.createJwt("access", 100L, "loginId", "ROLE_SELLER", 60 * 10 * 1000L);
    }

}
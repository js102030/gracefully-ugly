package com.gracefullyugly.domain.user.controller.api;

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

import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegResponse;
import com.gracefullyugly.domain.user.dto.FinalRegResponse;
import com.gracefullyugly.domain.user.dto.ProfileResponse;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import com.gracefullyugly.domain.user.dto.UpdateNicknameDto;
import com.gracefullyugly.domain.user.dto.UserResponse;
import com.gracefullyugly.domain.user.enumtype.Role;
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

    public static final String TEST_LOGIN_ID = "test100";
    public static final String TEST_NICKNAME = "testNickname";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_ADDRESS = "testAddress";
    public static final Role TEST_ROLE = Role.BUYER;
    public static final String NEW_NICKNAME = "newNickname";

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

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loginId\":\"test\",\"password\":\"test\"}"))
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
                .email(TEST_EMAIL)
                .address(TEST_ADDRESS)
                .build();

        given(userService.completeRegistration(100L, additionalRegRequest))
                .willReturn(FinalRegResponse.builder()
                        .userId(100L)
                        .loginId(TEST_LOGIN_ID)
                        .nickname(TEST_NICKNAME)
                        .email(TEST_EMAIL)
                        .address(TEST_ADDRESS)
                        .role(TEST_ROLE)
                        .createdDate(LocalDateTime.now())
                        .build()
                );
        // When & Then
        mockMvc.perform(patch("/api/users/100/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                "{\"role\":\"BUYER\",\"nickname\":\"testNickname\",\"email\":\"test@test.com\",\"address\":\"testAddress\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(100L))
                .andExpect(jsonPath("$.loginId").value(TEST_LOGIN_ID))
                .andExpect(jsonPath("$.nickname").value(TEST_NICKNAME))
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
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
                                .updatedDate(LocalDateTime.now())
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
                .andExpect(jsonPath("$.updatedDate").exists())
                .andDo(print());

        verify(userSearchService).getUser(any(Long.class));
    }

    @Test
    @DisplayName("프로필 조회 테스트")
    void getProfileTest() throws Exception {
        // Given
        given(userSearchService.getProfile(100L))
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
                .willReturn(UpdateNicknameDto.builder()
                        .nickname(NEW_NICKNAME)
                        .build()
                );

        // When & Then
        mockMvc.perform(patch("/api/users/100/nickname")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"newNickname\"}"))
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

        // When
        mockMvc.perform(patch("/api/users/100/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"" + newPassword + "\"}"))
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
                .willReturn(UpdateAddressDto.builder()
                        .address(TEST_ADDRESS)
                        .build()
                );

        // When & Then
        mockMvc.perform(patch("/api/users/100/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"address\":\"testAddress\"}"))
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

        // When
        mockMvc.perform(delete("/api/users/100"))
                .andExpect(status().isNoContent())
                .andDo(print());

        // Then
        verify(userService).delete(100L);
    }


}
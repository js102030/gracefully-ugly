package com.gracefullyugly.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegResponse;
import com.gracefullyugly.domain.user.dto.FinalRegResponse;
import com.gracefullyugly.domain.user.enumtype.Role;
import jakarta.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootTest
class UserServiceTest {

    public static final String TEST_LOGIN_ID = "testId";
    public static final String PASSWORD = "testPassword";
    public static final String TEST_NICKNAME = "testNickname";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_ADDRESS = "testAddress";
    public static final Role TEST_ROLE = Role.BUYER;
    public static final String ID_VALID_MESSAGE = "아이디 입력은 필수입니다.";
    public static final String ROLE_VALID_MESSAGE = "역할은 필수입니다.";
    public static final String NICKNAME_VALID_MESSAGE = "닉네임 입력은 필수입니다.";
    public static final String EMAIL_VALID_MESSAGE = "이메일 입력은 필수입니다.";
    public static final String ADDRESS_VALID_MESSAGE = "주소 입력은 필수입니다.";
    public static final String PASSWORD_VALID_MESSAGE = "비밀번호 입력은 필수입니다.";

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
    }

    @MockBean
    UserService userService;

    @Test
    @DisplayName("기본 회원가입 테스트")
    void createBasicAccountTest() {
        // given
        BasicRegRequest request = new BasicRegRequest(TEST_LOGIN_ID, PASSWORD);
        Mockito.when(userService.createBasicAccount(request)).thenReturn(new BasicRegResponse(1L, TEST_LOGIN_ID));

        // when
        BasicRegResponse response = userService.createBasicAccount(request);

        // then
        assertEquals(TEST_LOGIN_ID, response.getLoginId());
        assertEquals(1L, response.getUserId());

        verify(userService, times(1)).createBasicAccount(request);
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void createBasicAccountFailTest() {
        // given
        BasicRegRequest request = new BasicRegRequest("", "");

        // when
        Set<ConstraintViolation<BasicRegRequest>> violations = validator.validate(request);

        // then
        assertEquals(4, violations.size());

        for (ConstraintViolation<BasicRegRequest> violation : violations) {
            System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
        }

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(messages.contains(ID_VALID_MESSAGE));
        assertTrue(messages.contains(PASSWORD_VALID_MESSAGE));
    }

    @Test
    @DisplayName("회원가입 완료 테스트")
    void completeRegistrationTest() {
        // given
        AdditionalRegRequest additionalRegRequest = AdditionalRegRequest.builder()
                .role(TEST_ROLE)
                .nickname(TEST_NICKNAME)
                .email(TEST_EMAIL)
                .address(TEST_ADDRESS)
                .build();

        Mockito.when(userService.completeRegistration(1L, additionalRegRequest)).thenReturn(
                FinalRegResponse.builder()
                        .userId(1L)
                        .loginId(TEST_LOGIN_ID)
                        .nickname(TEST_NICKNAME)
                        .email(TEST_EMAIL)
                        .address(TEST_ADDRESS)
                        .role(TEST_ROLE)
                        .createdDate(LocalDateTime.MIN)
                        .build()
        );

        // when
        FinalRegResponse finalRegResponse = userService.completeRegistration(1L, additionalRegRequest);

        // then
        assertEquals(1L, finalRegResponse.getUserId());
        assertEquals(TEST_LOGIN_ID, finalRegResponse.getLoginId());
        assertEquals(TEST_NICKNAME, finalRegResponse.getNickname());
        assertEquals(TEST_EMAIL, finalRegResponse.getEmail());
        assertEquals(TEST_ADDRESS, finalRegResponse.getAddress());
        assertEquals(TEST_ROLE, finalRegResponse.getRole());
        assertEquals(LocalDateTime.MIN, finalRegResponse.getCreatedDate());
        verify(userService, times(1)).completeRegistration(1L, additionalRegRequest);
    }

    @Test
    @DisplayName("회원가입 완료 실패 테스트")
    void completeRegistrationFailTest() {
        // given
        AdditionalRegRequest additionalRegRequest = AdditionalRegRequest.builder()
                .role(null)
                .nickname("")
                .email("")
                .address("")
                .build();

        // when
        Set<ConstraintViolation<AdditionalRegRequest>> violations = validator.validate(additionalRegRequest);

        // then
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size());

        Set<String> messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(messages.contains(ROLE_VALID_MESSAGE));
        assertTrue(messages.contains(NICKNAME_VALID_MESSAGE));
        assertTrue(messages.contains(EMAIL_VALID_MESSAGE));
        assertTrue(messages.contains(ADDRESS_VALID_MESSAGE));
    }

}
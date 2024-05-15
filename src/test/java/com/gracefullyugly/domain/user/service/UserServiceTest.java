package com.gracefullyugly.domain.user.service;

import static com.gracefullyugly.testutil.SetupDataUtils.ADDRESS_VALID_MESSAGE;
import static com.gracefullyugly.testutil.SetupDataUtils.ID_VALID_MESSAGE;
import static com.gracefullyugly.testutil.SetupDataUtils.NICKNAME_VALID_MESSAGE;
import static com.gracefullyugly.testutil.SetupDataUtils.PASSWORD;
import static com.gracefullyugly.testutil.SetupDataUtils.PASSWORD_VALID_MESSAGE;
import static com.gracefullyugly.testutil.SetupDataUtils.ROLE_VALID_MESSAGE;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ADDRESS;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_LOGIN_ID;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static com.gracefullyugly.testutil.SetupDataUtils.TEST_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegResponse;
import com.gracefullyugly.domain.user.dto.FinalRegResponse;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Transactional
@SpringBootTest
@Slf4j
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserSearchService userSearchService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void beforeEach() {
        log.info("before each");

        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
    }

    @AfterEach
    void afterEach() {
        log.info("after each");
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("기본 회원가입 테스트")
    void createBasicAccountTest() {
        // given
        BasicRegRequest request = new BasicRegRequest(TEST_LOGIN_ID + 1, PASSWORD + 1);

        // when
        BasicRegResponse response = userService.createBasicAccount(request);
        User findUser = userSearchService.findById(response.getUserId());

        // then
        assertEquals(TEST_LOGIN_ID + 1, response.getLoginId());
        assertEquals(findUser.getId(), response.getUserId());
    }

    @Test
    @DisplayName("(실패) 기본 회원가입 테스트")
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
        BasicRegRequest request = new BasicRegRequest(TEST_LOGIN_ID + 1, PASSWORD + 1);
        BasicRegResponse response = userService.createBasicAccount(request);

        AdditionalRegRequest additionalRegRequest = AdditionalRegRequest.builder()
                .role(TEST_ROLE)
                .nickname(TEST_NICKNAME + 1)
                .address(TEST_ADDRESS)
                .build();

        // when
        FinalRegResponse finalRegResponse = userService.completeRegistration(response.getUserId(),
                additionalRegRequest);

        // then
        assertEquals(response.getUserId(), finalRegResponse.getUserId());
        assertEquals(TEST_LOGIN_ID + 1, finalRegResponse.getLoginId());
        assertEquals(TEST_NICKNAME + 1, finalRegResponse.getNickname());
        assertEquals(TEST_ADDRESS, finalRegResponse.getAddress());
        assertEquals(TEST_ROLE, finalRegResponse.getRole());
    }


    @Test
    @DisplayName("(실패) 회원가입 완료 테스트")
    void completeRegistrationFailTest() {
        // given
        AdditionalRegRequest additionalRegRequest = AdditionalRegRequest.builder()
                .role(null)
                .nickname("")
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
        assertTrue(messages.contains(ADDRESS_VALID_MESSAGE));
    }

    @Test
    @DisplayName("닉네임 변경 테스트")
    void updateNicknameTest() {
        // given
        User findUser = userSearchService.findByNickname(TEST_NICKNAME);
        String newNickname = "newNickname";

        // when
        userService.updateNickname(findUser.getId(), newNickname);
        User updatedUser = userSearchService.findById(findUser.getId());

        // then
        assertEquals(newNickname, updatedUser.getNickname());
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    void updatePasswordTest() {
        // given
        User findUser = userSearchService.findByNickname(TEST_NICKNAME);
        String newPassword = "newPassword";

        // when
        userService.updatePassword(findUser.getId(), newPassword);
        User updatedUser = userSearchService.findById(findUser.getId());

        // then
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));
    }

    @Test
    @DisplayName("주소 변경 테스트")
    void updateAddressTest() {
        // given
        User findUser = userSearchService.findByNickname(TEST_NICKNAME);
        String newAddress = "newAddress";

        // when
        userService.updateAddress(findUser.getId(), newAddress);
        User updatedUser = userSearchService.findById(findUser.getId());

        // then
        assertEquals(newAddress, updatedUser.getAddress());
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void deleteUserTest() {
        // given
        User findUser = userSearchService.findByNickname(TEST_NICKNAME);

        // when
        userService.delete(findUser.getId());
        User deletedUser = userSearchService.findById(findUser.getId());

        // then
        assertTrue(deletedUser.isDeleted());
    }

    @Test
    @DisplayName("이메일 인증 테스트")
    void updateVerifyTest() {
        // given
        User findUser = userSearchService.findByNickname(TEST_NICKNAME);
        String newEmail = "newEmail";

        // when
        userService.updateVerify(findUser.getId(), newEmail);
        User updatedUser = userSearchService.findById(findUser.getId());

        // then
        assertEquals(newEmail, updatedUser.getEmail());
        assertTrue(updatedUser.isVerified());
    }


}
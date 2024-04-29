package com.gracefullyugly.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.user.dto.UserResponse;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import com.gracefullyugly.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UserSearchServiceTest {

    public static final String TEST_LOGIN_ID = "testId";
    public static final String PASSWORD = "testPassword";
    public static final String TEST_NICKNAME = "testNickname";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String TEST_ADDRESS = "testAddress";

    @Autowired
    UserSearchService userSearchService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        User testUser = new User(
                null,
                SignUpType.GENERAL,
                Role.BUYER,
                TEST_LOGIN_ID,
                passwordEncoder.encode(PASSWORD),
                TEST_NICKNAME,
                TEST_EMAIL,
                TEST_ADDRESS,
                false,
                false,
                false);

        userRepository.save(testUser);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void getUserTest() {
        // given
        User findUser = userSearchService.findByNickname(TEST_NICKNAME);
        Long findUserId = findUser.getId();

        // when
        UserResponse userResponse = userSearchService.getUser(findUserId);

        // then
        assertThat(userResponse.getUserId()).isEqualTo(findUser.getId());
        assertThat(userResponse.getSignUpType()).isEqualTo(findUser.getSignUpType());
        assertThat(userResponse.getRole()).isEqualTo(findUser.getRole());
        assertThat(userResponse.getLoginId()).isEqualTo(findUser.getLoginId());
        assertThat(userResponse.getNickname()).isEqualTo(findUser.getNickname());
        assertThat(userResponse.getEmail()).isEqualTo(findUser.getEmail());
        assertThat(userResponse.getAddress()).isEqualTo(findUser.getAddress());
        assertThat(userResponse.isBanned()).isEqualTo(findUser.isBanned());
        assertThat(userResponse.isDeleted()).isEqualTo(findUser.isDeleted());
        assertThat(userResponse.isVerified()).isEqualTo(findUser.isVerified());
        assertThat(userResponse.getCreatedDate()).isEqualTo(findUser.getCreatedDate());
        assertThat(userResponse.getLastModifiedDate()).isEqualTo(findUser.getLastModifiedDate());
    }
}
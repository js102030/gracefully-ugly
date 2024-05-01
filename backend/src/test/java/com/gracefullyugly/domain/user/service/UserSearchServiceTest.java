package com.gracefullyugly.domain.user.service;

import static com.gracefullyugly.testutil.SetupDataUtils.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

import com.gracefullyugly.domain.user.dto.UserResponse;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import com.gracefullyugly.testutil.SetupDataUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class eUserSearchServiceTest {

    @Autowired
    UserSearchService userSearchService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(SetupDataUtils.makeTestUser(passwordEncoder));
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
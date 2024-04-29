package com.gracefullyugly.domain.user.service;

import com.gracefullyugly.domain.user.dto.ProfileResponse;
import com.gracefullyugly.domain.user.dto.UserDtoUtil;
import com.gracefullyugly.domain.user.dto.UserResponse;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserSearchService {

    private final UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + "에 해당하는 사용자가 없습니다."));
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException(nickname + "에 해당하는 사용자가 없습니다."));
    }

    // TODO buyCount, reviewCount 따로 가져와서 넣어주든 join으로 한번에 가져오든 해야함
    public ProfileResponse getProfile(Long userId) {
        User findUser = findById(userId);

        return UserDtoUtil.userToProfileResponse(findUser);
    }

    public UserResponse getUser(Long userId) {
        User findUser = findById(userId);

        return UserDtoUtil.userToUserResponse(findUser);
    }

    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean existsByNickName(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

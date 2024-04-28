package com.gracefullyugly.domain.user.service;

import com.gracefullyugly.domain.user.dto.AdditionalRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegRequest;
import com.gracefullyugly.domain.user.dto.BasicRegResponse;
import com.gracefullyugly.domain.user.dto.FinalRegResponse;
import com.gracefullyugly.domain.user.dto.UpdateAddressDto;
import com.gracefullyugly.domain.user.dto.UpdateNicknameDto;
import com.gracefullyugly.domain.user.dto.UserDtoUtil;
import com.gracefullyugly.domain.user.entity.User;
import com.gracefullyugly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserSearchService userSearchService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public BasicRegResponse createBasicAccount(BasicRegRequest request) {
        User savedUser = userRepository.save(joinRequestToUser(request));

        return UserDtoUtil.userToJoinResponse(savedUser);
    }

    public FinalRegResponse completeRegistration(Long userId, AdditionalRegRequest request) {
        User findUser = userSearchService.findById(userId);

        findUser.completeRegistration(request);

        return UserDtoUtil.userToFinalRegResponse(findUser);
    }

    private User joinRequestToUser(BasicRegRequest request) {
        return new User(
                request.getLoginId(),
                passwordEncoder.encode(request.getPassword())
        );
    }

    public UpdateNicknameDto updateNickname(Long userId, String newNickname) {
        User findUser = userSearchService.findById(userId);

        return findUser.updateNickname(newNickname);
    }

    public void updatePassword(Long userId, String password) {
        User findUser = userSearchService.findById(userId);

        findUser.updatePassword(passwordEncoder.encode(password));
    }

    public UpdateAddressDto updateAddress(Long userId, String address) {
        User findUser = userSearchService.findById(userId);

        return findUser.updateAddress(address);
    }

    public void delete(Long userId) {
        User findUser = userSearchService.findById(userId);

        findUser.delete();
    }
}

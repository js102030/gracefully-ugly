package com.gracefullyugly.domain.user.dto;

import com.gracefullyugly.domain.user.entity.User;

public class UserDtoUtil {

    public static BasicRegResponse userToJoinResponse(User user) {
        return new BasicRegResponse(user.getId(), user.getLoginId());
    }

    public static FinalRegResponse userToFinalRegResponse(User user) {
        return FinalRegResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .build();
    }

    public static UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .signUpType(user.getSignUpType())
                .role(user.getRole())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .address(user.getAddress())
                .isBanned(user.isBanned())
                .isDeleted(user.isDeleted())
                .isVerified(user.isVerified())
                .createdDate(user.getCreatedDate())
                .lastModifiedDate(user.getLastModifiedDate())
                .build();
    }

    public static ProfileResponse toProfileResponse(User user, int reviewCount) {
        return ProfileResponse.builder()
                .userId(user.getId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .address(user.getAddress())
                .email(user.getEmail())
                .role(user.getRole())
                .isVerified(user.isVerified())
                .buyCount(0) //TODO 임시 값
                .reviewCount(reviewCount)
                .build();
    }
}

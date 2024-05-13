package com.gracefullyugly.domain.user.dto;

import com.gracefullyugly.domain.user.enumtype.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Long userId;
    private String loginId;
    private String nickname;
    private String address;
    private String email;
    private Role role;
    private boolean isVerified;
    private int buyCount;
    private int reviewCount;

}
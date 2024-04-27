package com.gracefullyugly.domain.user.dto;

import com.gracefullyugly.domain.user.enumtype.Role;
import com.gracefullyugly.domain.user.enumtype.SignUpType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long userId;
    private SignUpType signUpType;
    private Role role;
    private String loginId;
    private String nickname;
    private String email;
    private String address;
    private boolean isBanned;
    private boolean isDeleted;
    private boolean isVerified;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}

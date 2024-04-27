package com.gracefullyugly.domain.user.dto;

import com.gracefullyugly.domain.user.enumtype.Role;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FinalRegResponse {

    private Long userId;
    private String loginId;
    private String nickname;
    private String email;
    private String address;
    private Role role;
    private LocalDateTime createdDate;

}

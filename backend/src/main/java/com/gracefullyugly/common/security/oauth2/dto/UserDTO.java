package com.gracefullyugly.common.security.oauth2.dto;

import com.gracefullyugly.domain.user.enumtype.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Role role;
    private String username;
    private Long userId;
}
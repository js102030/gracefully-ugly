package com.gracefullyugly.domain.user.dto;

import com.gracefullyugly.domain.user.enumtype.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdditionalRegRequest {

    private Role role;
    private String nickname;
    private String email;
    private String address;

}

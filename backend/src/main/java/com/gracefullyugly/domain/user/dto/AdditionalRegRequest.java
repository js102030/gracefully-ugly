package com.gracefullyugly.domain.user.dto;

import com.gracefullyugly.domain.user.enumtype.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdditionalRegRequest {

    @NotNull(message = "역할은 필수입니다.")
    private Role role;

    @NotBlank(message = "닉네임 입력은 필수입니다.")
    private String nickname;

    @NotBlank(message = "이메일 입력은 필수입니다.")
    private String email;

    @NotBlank(message = "주소 입력은 필수입니다.")
    private String address;

}

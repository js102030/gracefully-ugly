package com.gracefullyugly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidNickname {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

}


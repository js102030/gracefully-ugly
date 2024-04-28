package com.gracefullyugly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateNicknameDto {

    @NotBlank(message = "닉네임 입력은 필수입니다.")
    private String nickname;

}

package com.gracefullyugly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidEmail {

    @NotBlank(message = "이메일은 필수입니다.")
    private String email;

}

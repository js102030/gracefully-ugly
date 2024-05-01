package com.gracefullyugly.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateOrderPhoneNumberRequest {

    @NotBlank
    @Size(min = 10, max = 11)
    private String phoneNumber;
}

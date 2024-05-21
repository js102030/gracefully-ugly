package com.gracefullyugly.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    @Valid
    @NotEmpty
    private List<OrderItemDto> itemIdList;

    @NotBlank
    private String address;

    @NotBlank
    @Size(min = 10, max = 11)
    private String phoneNumber;
}

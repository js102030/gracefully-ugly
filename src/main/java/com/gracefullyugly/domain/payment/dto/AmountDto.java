package com.gracefullyugly.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmountDto {

    private Integer total;
    private Integer tax_free;
    private Integer vat;
    private Integer point;
    private Integer discount;
    private Integer green_deposit;
}

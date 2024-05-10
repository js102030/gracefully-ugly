package com.gracefullyugly.domain.payment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSearchListResponse {

    private List<PaymentSearchResultDTO> paymentList;
}

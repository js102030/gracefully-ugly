package com.gracefullyugly.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardInfoDto {

    private String kakaopay_purchase_corp, kakaopay_purchase_corp_code, kakaopay_issuer_corp, kakaopay_issuer_corp_code;
    private String bin;
    private String card_type;
    private String install_month;
    private String approved_id;
    private String card_mid;
    private String interest_free_install;
    private String installment_type;
    private String card_item_code;
}

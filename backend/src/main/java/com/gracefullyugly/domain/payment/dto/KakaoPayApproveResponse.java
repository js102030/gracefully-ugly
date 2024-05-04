package com.gracefullyugly.domain.payment.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaoPayApproveResponse {

    private String aid, tid, cid, sid;
    private String partner_order_id, partner_user_id, payment_method_type;
    private AmountDto amount;
    private CardInfoDto cardInfo;
    private String item_name, item_code;
    private Integer quantity;
    private Date created_at, approved_at;
    private String payload;
}

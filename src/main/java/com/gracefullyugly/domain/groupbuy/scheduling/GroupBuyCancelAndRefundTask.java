package com.gracefullyugly.domain.groupbuy.scheduling;

import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.payment.dto.RefundInfo;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import com.gracefullyugly.domain.payment.service.PaymentService;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GroupBuyCancelAndRefundTask {

    private final GroupBuyRepository groupBuyRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    @Scheduled(cron = "1 * * * * *") // 매분 1초에 실행
    @Transactional
    public void updateExpiredGroupBuyToCanceled() {
        groupBuyRepository.updateExpiredGroupBuyToCanceled();
    }

    @Scheduled(cron = "20 * * * * *") // 매분 20초에 실행
    public void refundsForCancelledGroupBuys() throws MessagingException, UnsupportedEncodingException {
        List<RefundInfo> refundablePayments = paymentRepository.findRefundablePayments();

        for (RefundInfo refundInfo : refundablePayments) {
            paymentService.refundKakaoPay(refundInfo.getUserId(), refundInfo.getOrderId());
        }
    }

}

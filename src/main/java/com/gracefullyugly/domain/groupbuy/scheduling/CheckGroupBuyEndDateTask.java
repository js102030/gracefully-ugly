package com.gracefullyugly.domain.groupbuy.scheduling;

import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import com.gracefullyugly.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CheckGroupBuyEndDateTask {

    private final GroupBuyRepository groupBuyRepository;
    private final PaymentRepository paymentRepository;

    @Scheduled(cron = "1 * * * * *") // 매분 1초에 실행
    @Transactional
    public void updateExpiredGroupBuyToCanceled() {
        groupBuyRepository.updateExpiredGroupBuyToCanceled();
    }

    @Scheduled(cron = "20 * * * * *") // 매분 20초에 실행
    @Transactional
    public void refundsForCancelledGroupBuys() {
        paymentRepository.updatePaymentsToRefundedForCancelledGroupBuys();
    }

}

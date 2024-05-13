package com.gracefullyugly.domain.groupbuy.scheduling;

import com.gracefullyugly.domain.groupbuy.repository.GroupBuyRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class CheckGroupBuyEndDateTask {

    private final GroupBuyRepository groupBuyRepository;

    @Scheduled(cron = "0 0 0 * * *") // 매일 오전 12시에 실행
    @Transactional
    public void updateExpiredGroupBuyToCanceled() {
        groupBuyRepository.updateExpiredGroupBuyToCanceled();
        // TODO: 취소된 공동 구매 건에 대해 사용자에게 알림을 보내는 로직이 필요할 듯
    }
}

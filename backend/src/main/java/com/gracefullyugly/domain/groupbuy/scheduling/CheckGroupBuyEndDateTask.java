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
    }
}

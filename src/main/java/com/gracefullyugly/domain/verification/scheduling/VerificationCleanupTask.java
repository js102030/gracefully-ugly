package com.gracefullyugly.domain.verification.scheduling;

import com.gracefullyugly.domain.verification.repository.VerificationRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class VerificationCleanupTask {

    private final VerificationRepository verificationRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Transactional
    public void deleteExpiredVerifications() {
        Date now = new Date();
        verificationRepository.deleteAllByExpiryDateBefore(now);
    }
}

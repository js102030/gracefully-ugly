package com.gracefullyugly.domain.log.service;

import com.gracefullyugly.domain.log.entity.Log;
import com.gracefullyugly.domain.log.enumtype.LogLevel;
import com.gracefullyugly.domain.log.repository.LogRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LogService {

    private final LogRepository logRepository;

    public void saveLog(Long userId, String methodSignature, String message, LogLevel level, long executionTime) {
        Log log = Log.builder()
                .userId(userId)
                .logTime(LocalDateTime.now())
                .logLevel(level)
                .methodSignature(methodSignature)
                .logMessage(message)
                .executionTimeMs(executionTime)
                .build();

        logRepository.save(log);
    }
}

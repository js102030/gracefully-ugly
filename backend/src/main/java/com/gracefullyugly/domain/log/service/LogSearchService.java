package com.gracefullyugly.domain.log.service;

import com.gracefullyugly.common.wrapper.ApiResponse;
import com.gracefullyugly.domain.log.entity.Log;
import com.gracefullyugly.domain.log.repository.LogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LogSearchService {

    private final LogRepository logRepository;

    public Log findByLogId(Long logId) {
        return logRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException(logId + "에 해당하는 로그가 없습니다."));
    }

    public ApiResponse<List<Log>> findLogsByUserId(Long userId) {
        List<Log> userLogs = logRepository.findByUserId(userId);

        if (userLogs.isEmpty()) {
            throw new IllegalArgumentException(userId + "에 해당하는 로그가 없습니다.");
        }

        return new ApiResponse<>(userLogs.size(), userLogs);
    }
}

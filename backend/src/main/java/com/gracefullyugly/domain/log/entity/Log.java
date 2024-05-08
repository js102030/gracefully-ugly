package com.gracefullyugly.domain.log.entity;

import com.gracefullyugly.domain.log.enumtype.LogLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;

    private Long userId;

    private LocalDateTime logTime;

    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;

    private String methodSignature;

    private String logMessage;

    private Long executionTimeMs;


    @Builder
    public Log(Long userId, LocalDateTime logTime, LogLevel logLevel,
               String methodSignature, String logMessage, Long executionTimeMs) {
        this.userId = userId;
        this.logTime = logTime;
        this.logLevel = logLevel;
        this.methodSignature = methodSignature;
        this.logMessage = logMessage;
        this.executionTimeMs = executionTimeMs;
    }

}

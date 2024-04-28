package com.gracefullyugly.domain.log.entity;

import com.gracefullyugly.domain.log.enumtype.LogLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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

    private LogLevel logLevel;

    private String methodSignature;

    private String logMessage;

    private Long executionTimeMs;

}

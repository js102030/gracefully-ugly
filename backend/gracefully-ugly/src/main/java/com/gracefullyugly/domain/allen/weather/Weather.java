package com.gracefullyugly.domain.allen.weather;

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
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long id;

    private int currentTemperature;

    private int minTemperature;

    private int maxTemperature;

    private int precipitation;

    private String weatherCondition;

    private String airQualityIndex;

    private int humidity;

    private String overallWeather;

    private LocalDateTime createdAt;

}

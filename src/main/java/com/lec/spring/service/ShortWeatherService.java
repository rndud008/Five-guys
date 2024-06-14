package com.lec.spring.service;

import com.lec.spring.domain.WeatherInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShortWeatherService {
    @Transactional
    void saveShortWeatherForecastToDB(int nx, int ny, String baseDate, String baseTime);

    List<WeatherInfo> getShortWeatherForecast(int nx, int ny, String baseDate, String baseTime);
}

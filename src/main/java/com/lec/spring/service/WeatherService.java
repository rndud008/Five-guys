package com.lec.spring.service;

import com.lec.spring.domain.WeatherDTO;

import java.util.List;

public interface WeatherService {
    void saveWeatherInfo(Long areacode, int nx, int ny);
    List<WeatherDTO> getWeatherInfo(Long areacode, int nx, int ny);
}

package com.lec.spring.service;

import com.lec.spring.domain.WeatherDTO;

import java.util.List;

public interface WeatherService {
    void saveWeatherInfo_short(Long areacode, int nx, int ny);
    List<WeatherDTO> getWeatherInfo_short(Long areacode, int nx, int ny);
}

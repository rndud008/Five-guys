package com.lec.spring.service;

import com.lec.spring.domain.WeatherDTO;
import com.lec.spring.domain.WeatherDTO_2;

import java.util.List;

public interface WeatherService_2 {
    // 기온
    void saveWeatherInfo_1(Long areacode, String regId);
    // 날씨예보
    void saveWeatherInfo_2(Long areacode, String regId);

    // 기온
    List<WeatherDTO_2> getWeatherInfo_1(Long areacode, String regId);

    // 날씨예보
    List<WeatherDTO_2> getWeatherInfo_2(Long areacode, String regId);
}

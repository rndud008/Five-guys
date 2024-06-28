package com.lec.spring.service;

import com.lec.spring.domain.WeatherDTO;
import com.lec.spring.domain.WeatherDTO_2;

import java.util.List;

public interface WeatherService_2 {
    void saveWeatherInfo_middle(Long areacode, String regId);
    List<WeatherDTO_2> getWeatherInfo_middle(Long areacode, String regId);

}

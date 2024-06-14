package com.lec.spring.service;

import com.lec.spring.domain.WeatherInfo;
import java.util.List;

public interface DataService {
    void saveWeatherData(List<WeatherInfo> weatherInfoList);
}

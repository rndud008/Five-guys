package com.lec.spring.service;

import com.lec.spring.domain.WeatherInfo;
import com.lec.spring.repository.WeatherInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;

    @Override
    public void saveWeatherData(List<WeatherInfo> weatherInfoList) {
        for (WeatherInfo weatherInfo : weatherInfoList) {
            weatherInfoRepository.insertWeatherInfo(weatherInfo);
        }
    }
}

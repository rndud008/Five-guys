package com.lec.spring.repository;

import com.lec.spring.domain.WeatherInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface WeatherInfoRepository {

    void insertWeatherInfo(WeatherInfo weatherInfo);

}
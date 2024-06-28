package com.lec.spring.repository;


import com.lec.spring.domain.WeatherDTO_2;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeatherRepository_2 {
    WeatherDTO_2 findByAreacode(WeatherDTO_2 weather);
    void updateWeather_middle(WeatherDTO_2 weather);
    void insertWeather_middle(WeatherDTO_2 weather);
    List<WeatherDTO_2> findWeatherByAreacodeAndRegId(@Param("areacode") Long areacode, @Param("regId") String regId);


}


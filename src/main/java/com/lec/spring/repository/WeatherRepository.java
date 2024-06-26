package com.lec.spring.repository;

import com.lec.spring.domain.WeatherDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeatherRepository {
    WeatherDTO findByAreacodeFcstDateTime(WeatherDTO weather);
    void updateWeather(WeatherDTO weather);
    void insertWeather(WeatherDTO weather);
    List<WeatherDTO> findWeatherByAreacodeAndCoordinates(@Param("areacode") Long areacode, @Param("nx") int nx, @Param("ny") int ny);

}

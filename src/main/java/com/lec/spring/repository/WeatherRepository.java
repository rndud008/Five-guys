package com.lec.spring.repository;

import com.lec.spring.domain.WeatherDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


public interface WeatherRepository {
    void insertWeather(WeatherDTO weatherDTO);
}

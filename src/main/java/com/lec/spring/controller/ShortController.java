package com.lec.spring.controller;

import com.lec.spring.domain.WeatherInfo;
import com.lec.spring.service.ShortWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/weather")
public class ShortController {

    private final ShortWeatherService shortWeatherService;

    @Autowired
    public ShortController(ShortWeatherService shortWeatherService) {
        this.shortWeatherService = shortWeatherService;
    }

    @GetMapping("/{nx}/{ny}/{baseDate}/{baseTime}")
    public List<WeatherInfo> getShortWeather(@PathVariable int nx, @PathVariable int ny,
                                             @PathVariable String baseDate, @PathVariable String baseTime) {
        return shortWeatherService.getShortWeatherForecast(nx, ny, baseDate, baseTime);
    }




}

package com.lec.spring.controller;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.WeatherDTO;
import com.lec.spring.domain.WeatherDTO_2;
import com.lec.spring.service.AreaService;
import com.lec.spring.service.WeatherService;
import com.lec.spring.service.WeatherService_2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherService_2 weatherService_2;
    private AreaService areaService;

    @Autowired
    public WeatherController(WeatherService weatherService , WeatherService_2 weatherService_2, AreaService areaService) {
        this.weatherService = weatherService;
        this.areaService = areaService;
        this.weatherService_2 = weatherService_2;
    }

    // 단기예보
    @PostMapping("/update1")
    public void updateWeatherInfo_short(@RequestParam Long areacode) {
        Areacode area = areaService.findByAreaCode(areacode);
        weatherService.saveWeatherInfo_short(area.getAreacode(), area.getNx(), area.getNy());
    }

    @GetMapping("/getWeather1")
    public List<WeatherDTO> getWeatherInfo_short(@RequestParam Long areacode) {
        Areacode area = areaService.findByAreaCode(areacode);
        return weatherService.getWeatherInfo_short(area.getAreacode(), area.getNx(), area.getNy());
    }

    // 중기예보
    // 날씨, 기온
    @PostMapping("/update2")
    public void updateWeatherInfo_middle(@RequestParam Long areacode) {
        Areacode area = areaService.findByAreaCode(areacode);
        weatherService_2.saveWeatherInfo_middle(area.getAreacode(), area.getRegId());
    }

    @GetMapping("/getWeather2")
    public List<WeatherDTO_2> getWeatherInfo_middle(@RequestParam Long areacode) {
        Areacode area = areaService.findByAreaCode(areacode);
        return weatherService_2.getWeatherInfo_middle(area.getAreacode(), area.getRegId());
    }

}

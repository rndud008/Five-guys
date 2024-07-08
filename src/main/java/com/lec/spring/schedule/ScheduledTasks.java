package com.lec.spring.schedule;

import com.lec.spring.domain.Areacode;
import com.lec.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private WeatherService weatherService;
    @Autowired
    private WeatherService_2 weatherService_2;
    @Autowired
    private AreacodeService areacodeService;

    @Scheduled(cron = "0 0 7-23 * * ?")
    public void travelModified() throws IOException, URISyntaxException {
        travelPostService.modifiedtimeTravelPosts();
        System.out.println("travelPostServiceData travelPostUpdate successfully!");
    }

    @Scheduled(cron = "0 0 7-23 * * ?")
    public void weather(){
        List<Areacode> areacodeList = areacodeService.list();

        for(Areacode areacode: areacodeList){
            weatherService.saveWeatherInfo_short(areacode.getAreacode(), areacode.getNx(), areacode.getNy());

            weatherService_2.saveWeatherInfo_middle(areacode.getAreacode(),areacode.getRegId());

            System.out.println(areacode.getName() + " 날씨 업데이트 완료");
        }

    }

}

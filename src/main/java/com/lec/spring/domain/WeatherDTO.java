package com.lec.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {
    private long id;
    private Areacode areacode;
    private LastCallApiData lastCallApiData;
    private String tmn;
    private String tmx;
    private String sky;
    private String pop;
    private String pty;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
}

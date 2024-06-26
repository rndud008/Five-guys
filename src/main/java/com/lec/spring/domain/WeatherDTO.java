package com.lec.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {
    private long id;
    private Areacode areacode;
    private LastCallApiDate lastCallApiDate;
    private String TMN;
    private String TMX;
    private String SKY;
    private String POP;
    private String PTY;
    private String fcstDate;
    private String fcstTime;
}

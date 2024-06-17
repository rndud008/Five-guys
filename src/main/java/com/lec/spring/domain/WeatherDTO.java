package com.lec.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO {
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private String url;
}

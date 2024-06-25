package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherDTO_2 {
    private long id;
    private Areacode areacode;
    private LastCallApiData lastCallApiData;

    private String tmFc;

    private String taMin4;
    private String taMax4;

    private String wf4Am;
    private String wf4Pm;

    private String taMin5;
    private String taMax5;

    private String wf5Am;
    private String wf5Pm;

    private String taMin6;
    private String taMax6;

    private String wf6Am;
    private String wf6Pm;

    private String taMin7;
    private String taMax7;

    private String wf7Am;
    private String wf7Pm;

    public Areacode getAreacode() {
        return areacode;
    }

    public void setAreacode(Areacode areacode) {
        this.areacode = areacode;
    }
}


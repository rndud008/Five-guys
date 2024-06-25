package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Areacode {
    private Long areacode;
    private String name;
    private String regId;
    private String regId2;
    private int nx;
    private int ny;
}

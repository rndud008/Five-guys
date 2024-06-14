package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelClassDetail {
    private Long id;
    // 여행정보 유형분류 id
    private TravelType travelType;
    // 여행타입 객체
    private String name;
    // 여행정보 유형분류 이름
    private String code;
    // 여행정보 유형분류 코드
    private String decode;
    // 여행정보 유형분 상위 코드

}

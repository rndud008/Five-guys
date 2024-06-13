package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //클래스에 대해 getter, setter, toString, equals, hashCode 메서드를 자동으로 생성합니다
@NoArgsConstructor //파라미터가 없는 기본 생성자를 생성합니다.
@AllArgsConstructor //모든 필드를 포함하는 생성자를 생성합니다
@Builder // builder pattern 사용 가능
public class Areacode {

    private Long areacode; // id = areacode이다

    private String name;

    private String regId;
}

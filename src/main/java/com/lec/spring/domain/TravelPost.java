package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPost {
    private Long id;
    // 여행 정보 목록 id
    private TravelClassDetail travelClassDetail;
    // 여행 타입 객체
    private LastCallApiDate lastCallApiData;
    // api 호출 객체
    private Sigungucode sigungucode;
    // 지역 객체
    private String title;
    // 제목
    private String addr1;
    // 주소
    private String addr2;
    // 상세주소
    private String contentid;
    // 콘텐츠 id
    private String firstimage;
    // 대표이미지 원본
    private String firstimage2;
    // 대표이미지 썸네일
    private String cpyrhtDivCd;
    // 저작권 유형
    private Double mapx;
    // gps X좌표
    private Double mapy;
    // gps Y좌표
    private String modifiedtime;
    // 수정일
    private String tel;
    // 전화번호
    private String eventstartdate;
    // 축제 : 행사시작일
    private String eventenddate;
    // 축제 : 행사종료일
    private String homepage;
    // 공통정보 : 홈페이지
    private String overview;
    // 공통정보 : 개요
    private String infocenter;
    // 관광지 소개정보 : 문의및 안내.
    private String parking;
    // 과광지 소개정보 : 주차시설
    private String restdate;
    // 관광지 소개정보 : 휴무일
    private String usetime;
    // 관광지 소개정보 : 이용시간
    private String eventplace;
    // 축제 소개정보 : 행사정보
    private String playtime;
    // 축제 소개정보 : 공연시간
    private String usetimefestival;
    // 축제 소개정보 : 이용요금
    private String infoname;
    // 축제 반복내용 : 행사내용 title 확인용.
    private String infotext;
    // 축제 반복내용 : 행사내용

}

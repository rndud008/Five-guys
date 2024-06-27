package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPost {
    private Long id;
    // 여행 정보 목록 id
    private TravelClassDetail travelClassDetail;
    // 여행 타입 객체
    private LastCallApiDate lastCallApiDate;
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
    private String fomatterStartDate;
    // 축제 시작일 - DB 저장 X
    private String status;
    // 축제 진행상태? - DB 저장 X
    private long daysUtils;
    // 축제 남은 시작일 - DB 저장 X


    public void preparaData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate startDate = LocalDate.parse(this.eventstartdate, formatter);
        LocalDate endDate = LocalDate.parse(this.eventenddate, formatter);
        LocalDate today = LocalDate.now();
        this.fomatterStartDate = startDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        if(today.isBefore(endDate)){
            this.status = "진행중";
            this.daysUtils = 0;

            if(today.isBefore(startDate)){
                this.daysUtils = ChronoUnit.DAYS.between(today,startDate);
            }

        }

    }

    public void defaultImageData(){
        String[] defaultImage = new String[]{
                "/default/default1.jpg","/default/default2.jpg","/default/default3.jpg","/default/default4.jpg","/default/default5.jpg",
                "/default/default6.jpg"};

        if(this.firstimage == null || this.firstimage.isEmpty()){
            Random rand = new Random();
            this.firstimage = defaultImage[rand.nextInt(defaultImage.length)];
        }
    }

}



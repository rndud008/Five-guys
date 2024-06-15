package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.TravelType;

import java.util.List;

public interface TravelPostRepository {
    int save(TravelPost travelPost);
    // 여행 정보 저장
    int update(TravelPost travelPost);
    // 여행 정보 업데이트
    int delete(TravelPost travelPost);
    // 특정 여행타입 정보세부 삭제.
    List<TravelPost> findTravelTypeByPostAll(TravelType travelType);
    // 특정 여행 타입 전체정보 조회
    List<TravelPost> findAreacodeAndTravelType(Areacode areacode, TravelType travelType);
    // 특정 지역 포함 여행타입 전체정보 조회
    List<TravelPost> findTravelTypeByTitle(TravelType travelType, String title);
    // 특정 여행타입 정보 제목조회 <검색용>
    List<TravelPost> findTravelTypeByModified(TravelType travelType, String modifiedTime);
    // 특정 여행정보 업데이트 리스트 -> last api 활용예정?
    TravelPost findByContentIdAndType(String contentid, TravelType travelType);
    // 특정 콘텐츠id 여행타입 을 포함한 정보 조회 <블로그,공통,소개,반복 api 활용예정.>


    // 지역기반 정보 조회 url
    // https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=%s&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&contentTypeId=%d

    //

}

package com.lec.spring.repository;

import com.lec.spring.domain.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelPostRepository {
    int save(TravelPost travelPost);
    // 여행 정보 저장

    int update(TravelPost travelPost);
    // 여행 정보 업데이트

    int delete(TravelPost travelPost);
    // 특정 여행타입 정보세부 삭제.

    List<TravelPost> findAll();
    // 네이버 블로그 데이터 주입용

    List<TravelPost> findTravelTypeByPostAll(@Param("travelClassDetail") TravelClassDetail travelClassDetail);
    // 특정 여행 타입 전체정보 조회

    List<TravelPost> findAreacodeAndTravelType(Sigungucode sigungucode, @Param("travelClassDetail") TravelClassDetail travelClassDetail);
    // 특정 지역 포함 여행타입 전체정보 조회

    List<TravelPost> findTravelTypeByTitle(@Param("travelClassDetail") TravelClassDetail travelClassDetail, String title);
    // 특정 여행타입 정보 제목조회 <검색용>

    List<TravelPost> findTravelTypeByModified(@Param("travelClassDetail") TravelClassDetail travelClassDetail, String modifiedTime);
    // 특정 여행정보 업데이트 리스트 -> last api 활용예정?

    TravelPost findByContentIdAndType(String contentid,@Param("travelClassDetail") TravelClassDetail travelClassDetail);
    // 특정 콘텐츠id 여행타입 을 포함한 정보 조회 <블로그,공통,소개,반복 api 활용예정.>

    TravelPost findPostByContentId(String contentId);


}

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
    List<TravelPost> findAreacodeAndTravelType(Sigungucode sigungucode, @Param("travelClassDetail") TravelClassDetail travelClassDetail);

    // 특정 지역 포함 여행타입 전체정보 조회
    List<TravelPost> findTravelTypeByTitleList(@Param("travelClassDetail") TravelClassDetail travelClassDetail, String title);

    // 특정 여행타입 정보 제목조회 <검색용>
    List<TravelPost> findTravelTypeByModified(@Param("travelClassDetail") TravelClassDetail travelClassDetail, String modifiedTime);

    // 특정 여행정보 업데이트 리스트 -> last api 활용예정?
    TravelPost findByContentIdAndType(String contentid, @Param("travelClassDetail") TravelClassDetail travelClassDetail);

    // 특정 콘텐츠id 여행타입 을 포함한 정보 조회 <블로그,공통,소개,반복 api 활용예정.>
    List<TravelPost> findTravelTypeByAreacodeTotalList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucodeList") List<Sigungucode> sigungucodeList,
            @Param("travelTypeId") long travelTypeId);
    // 특정타입 과 해당지역 시군구리스트 총데이터수 확인

    List<TravelPost> findTravelTypeByAreacodeList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucodeList") List<Sigungucode> sigungucodeList,
            @Param("travelTypeId") long travelTypeId,@Param("limit")int limit,@Param("offset")int offset);
    // 특정타입 과 해당지역 시군구리스트

    List<TravelPost> findTravelTypeByAreacodeAndSearchTotalList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucodeList") List<Sigungucode> sigungucodeList,
            @Param("travelTypeId") long travelTypeId, @Param("searchQuery")String searchQuery);
    // 특정타입 과 해당지역 시군구리스트 검색리스트 총데이터수 확인

    List<TravelPost> findTravelTypeByAreacodeAndSearchList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucodeList") List<Sigungucode> sigungucodeList,
            @Param("travelTypeId") long travelTypeId, @Param("searchQuery")String searchQuery,@Param("limit")int limit,@Param("offset")int offset);
    // 특정타입 과 해당지역 시군구리스트 검색리스트

    List<TravelPost> findTravelTypeByAreacodeAndSigungucodeTotalList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucode") Sigungucode sigungucode,
            @Param("travelTypeId") long travelTypeId);
    // 특정타입 과 해당지역 단일시군구 리스트 총데이터수 확인

    List<TravelPost> findTravelTypeByAreacodeAndSigungucodeList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucode") Sigungucode sigungucode,
            @Param("travelTypeId") long travelTypeId,@Param("limit")int limit,@Param("offset")int offset);
    // 특정타입 과 해당지역 단일시군구 리스트

    List<TravelPost> findTravelTypeByAreacodeAndSigungucodeAndSearchTotalList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucode") Sigungucode sigungucode,
            @Param("travelTypeId") long travelTypeId, @Param("searchQuery")String searchQuery);
    // 특정타입 과 해당지역 단일시군구 검색리스트 총데이터수 확인

    List<TravelPost> findTravelTypeByAreacodeAndSigungucodeAndSearchList(
            @Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList, @Param("sigungucode") Sigungucode sigungucode,
            @Param("travelTypeId") long travelTypeId, @Param("searchQuery")String searchQuery,@Param("limit")int limit,@Param("offset")int offset);
    // 특정타입 과 해당지역 단일시군구 검색리스트

    List<TravelPost> findByTravelTypeTotalList(@Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList
            , @Param("travelTypeId") long travelTypeId);
    // 특정 여행 타입 전체정보 조회 총데이터수 확인

    List<TravelPost> findByTravelTypeList(@Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList
            , @Param("travelTypeId") long travelTypeId,@Param("limit")int limit,@Param("offset")int offset);
    // 특정 여행 타입 전체정보 조회

    List<TravelPost> findByTravelTypAndSearchTotalList(@Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList
            , @Param("travelTypeId") long travelTypeId, @Param("searchQuery")String searchQuery);
    // 특정 여행 타입 전체정보 조회 검색리스트 총데이터수 확인

    List<TravelPost> findByTravelTypAndSearchList(@Param("travelClassDetailList") List<TravelClassDetail> travelClassDetailList
            , @Param("travelTypeId") long travelTypeId, @Param("searchQuery")String searchQuery,@Param("limit")int limit,@Param("offset")int offset);
    // 특정 여행 타입 전체정보 조회 검색리스트

    TravelPost findPostByContentId(String id);
    int addTravelPostLike(@Param("postId")Long postId,@Param("userId")Long userId);
    int deleteTravelPostLike(@Param("postId")Long postId,@Param("userId")Long userId);
    Long findLike(@Param("postId")Long postId, @Param("userId")Long userId);
    Long countLike(@Param("postId")Long postId);


}

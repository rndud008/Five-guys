package com.lec.spring.service;

import com.lec.spring.domain.Sigungucode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface TravelPostService {
    void saveTravelPosts() throws IOException, URISyntaxException;
    void updateApiErrorTravelPosts() throws IOException, URISyntaxException;
    TravelPost update(TravelPost travelPost);

    List<TravelPost> selectedTravelTypeByTitleList(TravelClassDetail travelClassDetail,String title);

    List<TravelPost> selectedByOverviewIsNull(List<TravelClassDetail> travelClassDetailList);

    List<TravelPost> selectedTravelTypeByAreacodeTotalList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList, long travelTypeId);
    // 특정타입 과 해당지역 시군구리스트 총데이터수 확인
    List<TravelPost> selectedTravelTypeByAreacodeList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList, long travelTypeId,int limit, int offset);

    List<TravelPost> selectedTravelTypeByAreacodeAndSearchTotalList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList, long travelTypeId, String searchQuery);
    // 특정타입 과 해당지역 시군구리스트 검색리스트 총데이터수 확인
    List<TravelPost> selectedTravelTypeByAreacodeAndSearchList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList, long travelTypeId, String searchQuery,int limit, int offset);

    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeTotalList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode, long travelTypeId);
    // 특정타입 과 해당지역 단일시군구 리스트 총데이터수 확인
    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode, long travelTypeId,int limit, int offset);

    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeAndSearchTotalList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode, long travelTypeId,String searchQuery);
    // 특정타입 과 해당지역 단일시군구 검색리스트 총데이터수 확인
    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeAndSearchList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode, long travelTypeId, String searchQuery,int limit, int offset);

    List<TravelPost> selectedByTravelTypeTotalList(List<TravelClassDetail> travelClassDetailList,long travelTypeId);
    // 특정 여행 타입 전체정보 조회 총데이터수 확인
    List<TravelPost> selectedByTravelTypeList(List<TravelClassDetail> travelClassDetailList, long travelTypeId,int limit, int offset);

    List<TravelPost> selectedByTravelTypAndSearchTotalList(List<TravelClassDetail> travelClassDetailList, long travelTypeId, String searchQuery);
    // 특정 여행 타입 전체정보 조회 검색리스트 총데이터수 확인
    List<TravelPost> selectedByTravelTypAndSearchList(List<TravelClassDetail> travelClassDetailList, long travelTypeId, String searchQuery,int limit, int offset);

    TravelPost getTravelPostBycontentId(String id) throws IOException;

    int saveLike(Long postId, Long userId);
    int deleteLike(Long postId, Long userId);
    Long selectedLike(Long postId, Long userId);
    Long totalLike(Long postId);

}

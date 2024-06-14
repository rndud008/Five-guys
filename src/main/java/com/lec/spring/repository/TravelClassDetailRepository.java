package com.lec.spring.repository;

import com.lec.spring.domain.Sigungucode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;

import java.util.List;

public interface TravelClassDetailRepository extends GenericRepository<TravelClassDetail>{
    int save(TravelClassDetail travelClassDetail);
    // 여행정보 유형분류 저장
    int update(TravelClassDetail travelClassDetail);
    // 여행정보 유형분류 수정
    List<TravelClassDetail> findAll();
    // 여행유형 전체목록
    TravelClassDetail findByCode(String code);
    // 특정 여행유형 찾기
    TravelClassDetail findDecodeByCode(String code, String decode);
    // 특정 여행하위유형 찾기

}

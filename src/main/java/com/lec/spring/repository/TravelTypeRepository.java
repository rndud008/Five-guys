package com.lec.spring.repository;

import com.lec.spring.domain.TravelType;

import java.util.List;

public interface TravelTypeRepository {

    List<TravelType> findAll();
    // 여행타입 전체목록 조회

    TravelType findById(Long id);
    // 특정 여행타입 id 조회
}

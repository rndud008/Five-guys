package com.lec.spring.service;

import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface TravelClassDetailService {
    void saveTravelClassDetails() throws IOException, URISyntaxException;
    TravelClassDetail selectedByTravelTypeId(TravelType travelType);
    List<TravelClassDetail> selectedTravelTypeByCodeAndDecodeList(TravelType travelType, String code, String decode);
    // 해당 타입에 대한 각 코드 / 상위코드 리스트 받아오기
    List<TravelClassDetail> selectedByTravelTypeList(TravelType travelType);
    // 해당 타입의 리스트 가져오기
    List<TravelClassDetail> selectedTravelTypeIdByDecode(TravelType travelType, String decode);
    List<TravelClassDetail> selectedTravelTypeByCodeList(TravelType travelType, String code);
    // 여행타입 code 유형의 리스트



}

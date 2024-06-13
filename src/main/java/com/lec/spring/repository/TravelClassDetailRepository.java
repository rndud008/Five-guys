package com.lec.spring.repository;

import com.lec.spring.domain.TravelClassDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TravelClassDetailRepository {

    void insertTravelClassDetail(TravelClassDetail travelClassDetail);

}

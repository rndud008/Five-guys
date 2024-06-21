package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.TravelClassDetailRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TravelClassTransacionService {

    private TravelClassDetailRepository travelClassDetailRepository;

    @Autowired
    public TravelClassTransacionService(SqlSession sqlSession){
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
    }

    @Transactional
    public void travelClassDetailSave(JsonNode item, TravelType travelType){
        TravelClassDetail travelClassDetail = new TravelClassDetail();
        travelClassDetail.setTravelType(travelType);
        travelClassDetail.setCode(item.get("code").asText());
        travelClassDetail.setName(item.get("name").asText());
        travelClassDetailRepository.save(travelClassDetail);
        System.out.println("item 저장완료");
    }

    @Transactional
    public void travelClassDetailSave(JsonNode item, TravelType travelType, TravelClassDetail travelClassDetail){
        TravelClassDetail travelClassDetail1 = new TravelClassDetail();
        travelClassDetail1.setTravelType(travelType);
        travelClassDetail1.setCode(item.get("code").asText());
        travelClassDetail1.setName(item.get("name").asText());
        travelClassDetail1.setDecode(String.valueOf(travelClassDetail.getCode()));
        travelClassDetailRepository.save(travelClassDetail1);
        System.out.println("item2 저장완료");
    }

}

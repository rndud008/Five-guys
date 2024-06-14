package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.Sigungucode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.TravelClassDetailRepository;
import com.lec.spring.repository.TravelTypeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class TravelClassDetailServiceImpl implements TravelClassDetailService {

    private TravelTypeRepository travelTypeRepository;
    private TravelClassDetailRepository travelClassDetailRepository;
    private DataService dataService;

    @Autowired
    public TravelClassDetailServiceImpl(SqlSession sqlSession, DataService dataService) {
        travelTypeRepository = sqlSession.getMapper(TravelTypeRepository.class);
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
        this.dataService = dataService;
    }

    @Override
    public void saveTravelClassDetails() throws IOException, URISyntaxException {
        String apikey = "mcw7keMXaCfirqxNz26s6jfbbhIQavF0pTNbArIUT1RLEdHm%2BYx92V%2FJswNwZJJvPhglAPqs%2BAMGMzcqDsuLEQ%3D%3D";
        List<TravelType> travelTypes = travelTypeRepository.findAll();
        for (TravelType travelType : travelTypes) {

            String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d", apikey, travelType.getId());
            System.out.println(apiUrl);

            JsonNode items = dataService.fetchApiData(apiUrl);

            System.out.println("push 시작");
            for (JsonNode item : items) {
                String code = item.get("code").asText();
                if (travelClassDetailRepository.findByCode(code) == null) {
                    TravelClassDetail travelClassDetail = new TravelClassDetail();
                    travelClassDetail.setTravelType(travelType);
                    travelClassDetail.setCode(item.get("code").asText());
                    travelClassDetail.setName(item.get("name").asText());
                    travelClassDetailRepository.save(travelClassDetail);
                }
            }

//            List<TravelClassDetail> travelClassDetails = travelClassDetailRepository.findAll();
//            for (TravelClassDetail travelClassDetail : travelClassDetails) {
//                String apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
//                        "&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s", apikey, travelType.getId(), travelClassDetail.getCode());
//                System.out.println(apiUrl2);
//                JsonNode items2 = dataService.fetchApiData(apiUrl2);
//                System.out.println("push2 시작");
//                for (JsonNode item : items2) {
//                    TravelClassDetail travelClassDetail2 = new TravelClassDetail();
//                    travelClassDetail2.setTravelType(travelType);
//                    travelClassDetail2.setCode(item.get("code").asText());
//                    travelClassDetail2.setName(item.get("name").asText());
//                    travelClassDetail2.setDecode(String.valueOf(travelClassDetail.getCode()));
//                    travelClassDetailRepository.save(travelClassDetail2);
//                }
//            }


        }

    }
}

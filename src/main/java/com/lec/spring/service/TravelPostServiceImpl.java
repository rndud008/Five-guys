package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.*;
import com.lec.spring.repository.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TravelPostServiceImpl implements TravelPostService {
    @Value("${app.apikey}")
    private String apikey;
    private TravelTypeRepository travelTypeRepository;
    private TravelClassDetailRepository travelClassDetailRepository;
    private TravelPostRepository travelPostRepository;
    private AreacodeRepository areacodeRepository;
    private SigungucodeRepository sigungucodeRepository;
    private LastCallApiDataRepository lastCallApiDataRepository;
    private DataService dataService;
    private TravelPostTransacionService travelPostTransacionService;

    static String BASE_URL = "https://apis.data.go.kr/B551011/KorService1/";

    @Autowired
    public TravelPostServiceImpl(SqlSession sqlSession, DataService dataService,
                                 TravelPostTransacionService travelPostTransacionService) {
        travelTypeRepository = sqlSession.getMapper(TravelTypeRepository.class);
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
        travelPostRepository = sqlSession.getMapper(TravelPostRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        sigungucodeRepository = sqlSession.getMapper(SigungucodeRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
        this.dataService = dataService;
        this.travelPostTransacionService = travelPostTransacionService;
    }

    @Override
    public void saveTravelPosts() throws IOException, URISyntaxException {

        LastCallApiData detailCommon1 = new LastCallApiData(); // 공통정보 url
        LastCallApiData detailIntro1 = new LastCallApiData(); // 소개정보 url
        LastCallApiData detailInfo1 = new LastCallApiData(); // 반복정보 url

        List<TravelType> travelTypes = travelTypeRepository.findAll();
        System.out.println("travelTypes 시작");
        for (TravelType travelType : travelTypes) {
            String apiUrl = null;
            apiUrl = String.format(BASE_URL + "areaBasedList1?serviceKey=%s" +
                    "&numOfRows=1000&pageNo=5&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&" +
                    "contentTypeId=%d", apikey, travelType.getId());
            System.out.println(apiUrl);
            JsonNode items = null;

            items = dataService.fetchApiData(apiUrl);

//            timeUnit();

            if (items != null) {

                for (JsonNode item : items) {

                    try {

                        travelPostTransacionService.itemSave(item, travelType, detailInfo1, detailCommon1, detailIntro1);

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                        System.out.println( "contentid : " + item.get("contentid").asText());
                        System.out.println( "contenttypeid" + travelType.getId());
                        System.out.println("item 저장 실패");
                    }

//                    timeUnit();
                }// end item
            }//end items if문
        }// end travelTypes
    }// end saveTravelPosts

    @Override
    public TravelPost getTravelPostById(String id) throws IOException {
        return travelPostRepository.findPostByContentId(id);
    }

    public void timeUnit() {
        // API 호출 간격을 두기 위해 잠시 대기
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("timeUnit 실행");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}

package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.*;
import com.lec.spring.repository.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class TravelPostServiceImpl implements TravelPostService {
    @Value("${app.apikey}")
    private String apikey;
    private TravelTypeRepository travelTypeRepository;
    private TravelClassDetailRepository travelClassDetailRepository;
    private TravelPostRepository travelPostRepository;
    private AreacodeRepository areacodeRepository;
    private SigungucodeRepository sigungucodeRepository;
    private LastCallApiDateRepository lastCallApiDataRepository;
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
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
        this.dataService = dataService;
        this.travelPostTransacionService = travelPostTransacionService;
    }

    @Override
    public void saveTravelPosts() throws IOException, URISyntaxException {

        LastCallApiDate detailCommon1 = new LastCallApiDate(); // 공통정보 url
        LastCallApiDate detailIntro1 = new LastCallApiDate(); // 소개정보 url
        LastCallApiDate detailInfo1 = new LastCallApiDate(); // 반복정보 url

        List<TravelType> travelTypes = travelTypeRepository.findAll();
        System.out.println("travelTypes 시작");
        for (TravelType travelType : travelTypes) {
            String apiUrl = null;
            apiUrl = String.format(BASE_URL + "areaBasedList1?serviceKey=%s" +
                    "&numOfRows=1000&pageNo=13&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&" +
                    "contentTypeId=%d", apikey, travelType.getId());
            System.out.println(apiUrl);
            JsonNode items = null;

            items = dataService.fetchApiData(apiUrl);

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

                }// end item
            }//end items if문
        }// end travelTypes
    }// end saveTravelPosts

    @Override
    public List<TravelPost> selectedTravelTypeByTitleList(TravelClassDetail travelClassDetail, String title) {

        List<TravelPost> travelPostList = null;
        try {
            travelPostList = travelPostRepository.findTravelTypeByTitleList(travelClassDetail, title);
            System.out.println("Result size: " + travelPostList.size());
            System.out.println(travelClassDetail.getTravelType().getId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(); // 더 자세한 로그 출력
        }
        return travelPostList;

    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeList(List<TravelClassDetail> travelClassDetailList,
                                                             List<Sigungucode> sigungucodeList) {
        return travelPostRepository.findTravelTypeByAreacodeList(travelClassDetailList,sigungucodeList);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode) {
        return travelPostRepository.findTravelTypeByAreacodeAndSigungucodeList(travelClassDetailList, sigungucode);
    }

    @Override
    public List<TravelPost> selectedByTravelTypeList( List<TravelClassDetail> travelClassDetailList) {
        return travelPostRepository.findByTravelTypeList(travelClassDetailList);
    }

}

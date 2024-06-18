package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.LastCallApiData;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.LastCallApiDataRepository;
import com.lec.spring.repository.TravelClassDetailRepository;
import com.lec.spring.repository.TravelTypeRepository;
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
public class TravelClassDetailServiceImpl implements TravelClassDetailService {
    @Value("${app.apikey}")
    private String apikey;
    private TravelTypeRepository travelTypeRepository;
    private TravelClassDetailRepository travelClassDetailRepository;
    private LastCallApiDataRepository lastCallApiDataRepository;
    private DataService dataService;
    private TravelClassTransacionService travelClassTransacionService;


    @Autowired
    public TravelClassDetailServiceImpl(SqlSession sqlSession, DataService dataService, TravelClassTransacionService travelClassTransacionService) {
        travelTypeRepository = sqlSession.getMapper(TravelTypeRepository.class);
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
        this.dataService = dataService;
        this.travelClassTransacionService = travelClassTransacionService;
    }

    @Override
    public void saveTravelClassDetails() throws IOException, URISyntaxException {

        List<TravelType> travelTypes = travelTypeRepository.findAll();
        System.out.println("travelTypes 시작");

        LastCallApiData lastCallApiData = new LastCallApiData();
        for (TravelType travelType : travelTypes) {


            String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?" +
                    "serviceKey=%s&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d", apikey, travelType.getId());
            System.out.println(apiUrl);

            JsonNode items = null;

            items = dataService.fetchApiData(apiUrl);

            for (JsonNode item : items) {

                String code = item.get("code").asText();
                if (travelClassDetailRepository.findTravelTypeIdByCode(travelType, code) == null) {
                    travelClassTransacionService.travelClassDetailSave(item, travelType);
                }else {
                    System.out.println("item은 이미 저장 되어 있음.");// travelClassDetail save
                }

                timeUnit();

                lastSave(travelType, lastCallApiData);

            }// end item


        }// end travelType
    }

    public void lastSave(TravelType travelType, LastCallApiData lastCallApiData) throws IOException, URISyntaxException {
        String apiUrl2 = null;
        JsonNode items2 = null;

        List<TravelClassDetail> travelClassDetails = travelClassDetailRepository.findByTravelTypeList(travelType);
        System.out.println("travelClassDetails 시작");
        for (TravelClassDetail travelClassDetail : travelClassDetails) {


            apiUrl2 = createApiUrl(travelClassDetail, travelType);

            if (lastCallApiDataRepository.findByUrl(apiUrl2) == null){
                System.out.println(apiUrl2);
                lastCallApiData.setUrl(apiUrl2);

                items2 = dataService.fetchApiData(apiUrl2);

                System.out.println("push2 시작");
                for (JsonNode item2 : items2) {
                    String code2 = item2.get("code").asText();
                    if (travelClassDetailRepository.findByCode(code2) == null) {

                        travelClassTransacionService.travelClassDetailSave(item2, travelType, travelClassDetail);

                    }// travelClassDetail2 save

                }// end items2
                lastCallApiDataRepository.save(lastCallApiData);
            }else {
                System.out.println(apiUrl2);
                System.out.println("이미 호출 완료");
            }

            timeUnit();

        }// end travelClassDetail

        List<TravelClassDetail> travelClassDetails2 = travelClassDetailRepository.findByTravelTypeList(travelType);
        System.out.println("travelClassDetails2 시작");
        for (TravelClassDetail travelClassDetail2 : travelClassDetails2) {

            apiUrl2 = createApiUrl(travelClassDetail2, travelType);

            if (lastCallApiDataRepository.findByUrl(apiUrl2) == null){
                System.out.println(apiUrl2);
                lastCallApiData.setUrl(apiUrl2);

                items2 = dataService.fetchApiData(apiUrl2);

                System.out.println("push2 시작");
                for (JsonNode item2 : items2) {
                    String code2 = item2.get("code").asText();
                    if (travelClassDetailRepository.findByCode(code2) == null) {

                        travelClassTransacionService.travelClassDetailSave(item2, travelType, travelClassDetail2);

                    }// travelClassDetail2 save

                }// end items2
                lastCallApiDataRepository.save(lastCallApiData);
            }else {
                System.out.println(apiUrl2);
                System.out.println("이미 호출 완료");
            }

            timeUnit();

        }//end travelClassDetail2

    }// end lastSave

    public String createApiUrl(TravelClassDetail travelClassDetail, TravelType travelType){

        String apiUrl2= null;
        if (travelClassDetail.getDecode() == null) {
            apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                            "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s"
                    , apikey, travelType.getId(), travelClassDetail.getCode());
        } else if (travelClassDetail.getDecode().length() == 3) {
            apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                            "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s&cat2=%s"
                    , apikey, travelType.getId(), travelClassDetail.getDecode(), travelClassDetail.getCode());
        } else if (travelClassDetail.getDecode().length() == 5) {
            apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                            "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s&cat2=%s&cat3=%s"
                    , apikey, travelType.getId(), travelClassDetail.getDecode().substring(0, 3), travelClassDetail.getDecode(), travelClassDetail.getCode());
        }
        return apiUrl2;
    }

    public void timeUnit(){
        // API 호출 간격을 두기 위해 잠시 대기
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("timeUnit 실행");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



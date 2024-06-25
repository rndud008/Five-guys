package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.LastCallApiDateRepository;
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
    private LastCallApiDateRepository lastCallApiDateRepository;
    private DataService dataService;

    @Autowired
    public TravelClassDetailServiceImpl(SqlSession sqlSession, DataService dataService) {
        travelTypeRepository = sqlSession.getMapper(TravelTypeRepository.class);
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
        lastCallApiDateRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
        this.dataService = dataService;
    }

    @Override
    public void saveTravelClassDetails() throws IOException, URISyntaxException {

        List<TravelType> travelTypes = travelTypeRepository.findAll();
        System.out.println("travelTypes 시작");

        LastCallApiDate lastCallApiDate = new LastCallApiDate();
        for (TravelType travelType : travelTypes) {

            String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?" +
                    "serviceKey=%s&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d", apikey, travelType.getId());
            System.out.println(apiUrl);

            JsonNode items = null;
            try {

                items = dataService.fetchApiData(apiUrl);

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            for (JsonNode item : items) {

                String code = item.get("code").asText();
                if (travelClassDetailRepository.findTravelTypeIdByCode(travelType, code) == null) {
                    travelClassDetailSave(item, travelType);
                }// travelClassDetail save

                String apiUrl2 = null;

                timeUnit();

                List<TravelClassDetail> travelClassDetails = travelClassDetailRepository.findByTravelTypeList(travelType);
                System.out.println("travelClassDetails 시작");
                for (TravelClassDetail travelClassDetail : travelClassDetails) {

                    apiUrl2 = createApiUrl(travelClassDetail, travelType);

                    lastSave(apiUrl2, lastCallApiDate, travelType, travelClassDetail);

                    timeUnit();

                }// end travelClassDetail

                List<TravelClassDetail> travelClassDetails2 = travelClassDetailRepository.findByTravelTypeList(travelType);
                System.out.println("travelClassDetails2 시작");
                for (TravelClassDetail travelClassDetail2 : travelClassDetails2) {

                    apiUrl2 = createApiUrl(travelClassDetail2, travelType);

                    lastSave(apiUrl2, lastCallApiDate, travelType, travelClassDetail2);

                    timeUnit();

                }//end travelClassDetail2

            }// end item

        }// end travelType
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

    public void lastSave(String apiUrl2, LastCallApiDate lastCallApiDate, TravelType travelType, TravelClassDetail travelClassDetail) throws IOException, URISyntaxException {
        if (lastCallApiDateRepository.findByUrl(apiUrl2) == null){
            System.out.println(apiUrl2);
            lastCallApiDate.setUrl(apiUrl2);
            JsonNode items2 = dataService.fetchApiData(apiUrl2);
            System.out.println("push2 시작");
            for (JsonNode item2 : items2) {
                String code2 = item2.get("code").asText();
                if (travelClassDetailRepository.findByCode(code2) == null) {

                    travelClassDetailSave(item2, travelType, travelClassDetail);

                }// travelClassDetail2 save

            }// end items2
            lastCallApiDateRepository.save(lastCallApiDate);
        }else {
            System.out.println(apiUrl2);
            System.out.println("이미 호출 완료");
        }
    }

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
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("timeUnit 실행");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



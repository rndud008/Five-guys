package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.TravelClassDetailRepository;
import com.lec.spring.repository.TravelTypeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class TravelClassDetailServiceImpl implements TravelClassDetailService {
    @Value("${app.apikey}")
    private String apikey;
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

        List<TravelType> travelTypes = travelTypeRepository.findAll();
        System.out.println("travelTypes 시작");
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
                TravelType travelType1 = travelTypeRepository.findById(travelType.getId());
                if (travelClassDetailRepository.findTravelTypeIdByCode(travelType1, code) == null) {
                    TravelClassDetail travelClassDetail = new TravelClassDetail();
                    travelClassDetail.setTravelType(travelType);
                    travelClassDetail.setCode(item.get("code").asText());
                    travelClassDetail.setName(item.get("name").asText());
                    travelClassDetailRepository.save(travelClassDetail);
                    System.out.println("item 저장완료");
                }// travelClassDetail save

                String apiUrl2 = null;

                List<TravelClassDetail> travelClassDetails = travelClassDetailRepository.findByTravelTypeList(travelType);
                System.out.println("travelClassDetails 시작");
                for (TravelClassDetail travelClassDetail : travelClassDetails) {

                    if (travelClassDetail.getDecode() == null) {
//                        TravelClassDetail travelClassDetail1 = travelClassDetailRepository.findTravelTypeIdByCode(travelType1,travelClassDetail.getCode());
                        apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                                        "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s"
                                , apikey, travelType.getId(), travelClassDetail.getCode());
                    } else if (travelClassDetail.getDecode().length() == 3) {
//                        TravelClassDetail travelClassDetail1 = travelClassDetailRepository.findTravelTypeIdByCode(travelType1, travelClassDetail.getCode());
                        apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                                        "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s&cat2=%s"
                                , apikey, travelType.getId(), travelClassDetail.getDecode(), travelClassDetail.getCode());
                    } else if (travelClassDetail.getDecode().length() == 5) {
//                        TravelClassDetail travelClassDetail1 = travelClassDetailRepository.findTravelTypeIdByCode(travelType1, travelClassDetail.getCode());
                        apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                                        "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s&cat2=%s&cat3=%s"
                                , apikey, travelType.getId(), travelClassDetail.getDecode().substring(0, 3), travelClassDetail.getDecode(), travelClassDetail.getCode());
                    }

                    System.out.println(apiUrl2);
                    JsonNode items2 = dataService.fetchApiData(apiUrl2);
                    System.out.println("push2 시작");
                    for (JsonNode item2 : items2) {
                        String code2 = item2.get("code").asText();
                        if (travelClassDetailRepository.findByCode(code2) == null) {
                            TravelClassDetail travelClassDetail2 = new TravelClassDetail();
                            travelClassDetail2.setTravelType(travelType);
                            travelClassDetail2.setCode(item2.get("code").asText());
                            travelClassDetail2.setName(item2.get("name").asText());
                            travelClassDetail2.setDecode(String.valueOf(travelClassDetail.getCode()));
                            travelClassDetailRepository.save(travelClassDetail2);
                            System.out.println("item2 저장완료");
                        }// travelClassDetail2 save

                    }// end  items2

                }// end travelClassDetail

                List<TravelClassDetail> travelClassDetails2 = travelClassDetailRepository.findByTravelTypeList(travelType);
                System.out.println("travelClassDetails2 시작");
                for (TravelClassDetail travelClassDetail2 : travelClassDetails2) {

                    if (travelClassDetail2.getDecode() == null) {
//                        TravelClassDetail travelClassDetail1 = travelClassDetailRepository.findTravelTypeIdByCode(travelType1,travelClassDetail2.getCode());
                        apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                                        "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s"
                                , apikey, travelType.getId(), travelClassDetail2.getCode());
                    } else if (travelClassDetail2.getDecode().length() == 3) {
//                        TravelClassDetail travelClassDetail1 = travelClassDetailRepository.findTravelTypeIdByCode(travelType1, travelClassDetail2.getCode());
                        apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                                        "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s&cat2=%s"
                                , apikey, travelType.getId(), travelClassDetail2.getDecode(), travelClassDetail2.getCode());
                    } else if (travelClassDetail2.getDecode().length() == 5) {
//                        TravelClassDetail travelClassDetail1 = travelClassDetailRepository.findTravelTypeIdByCode(travelType1, travelClassDetail2.getCode());
                        apiUrl2 = String.format("https://apis.data.go.kr/B551011/KorService1/categoryCode1?serviceKey=%s" +
                                        "&numOfRows=50&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentTypeId=%d&cat1=%s&cat2=%s&cat3=%s"
                                , apikey, travelType.getId(), travelClassDetail2.getDecode().substring(0, 3), travelClassDetail2.getDecode(), travelClassDetail2.getCode());
                    }

                    System.out.println("travelClassDetails2");
                    JsonNode items3 = dataService.fetchApiData(apiUrl2);
                    System.out.println(apiUrl2);
                    System.out.println("push3 시작");
                    for (JsonNode item3 : items3) {

                        String code3 = item3.get("code").asText();
                        if (travelClassDetailRepository.findByCode(code3) == null) {
                            TravelClassDetail travelClassDetail3 = new TravelClassDetail();
                            travelClassDetail3.setTravelType(travelType);
                            travelClassDetail3.setCode(item3.get("code").asText());
                            travelClassDetail3.setName(item3.get("name").asText());
                            travelClassDetail3.setDecode(String.valueOf(travelClassDetail2.getCode()));
                            travelClassDetailRepository.save(travelClassDetail3);
                            System.out.println("item3 저장완료");
                        }// travelClassDetail3 save

                    }// end item3

                }//end travelClassDetail2

            }// end item

        }// end travelType
    }
}



package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.LastCallApiData;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private LastCallApiDataRepository lastCallApiDataRepository;
    private DataService dataService;
    static String BASE_URL = "https://apis.data.go.kr/B551011/KorService1/";

    @Autowired
    public TravelPostServiceImpl(SqlSession sqlSession, DataService dataService) {
        travelTypeRepository = sqlSession.getMapper(TravelTypeRepository.class);
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
        travelPostRepository = sqlSession.getMapper(TravelPostRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        sigungucodeRepository = sqlSession.getMapper(SigungucodeRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
        this.dataService = dataService;
    }

    @Override
    @Transactional
    public void saveTravelPosts() throws IOException, URISyntaxException {

        List<TravelType> travelTypes = travelTypeRepository.findAll();
        System.out.println("travelTypes 시작");
        for (TravelType travelType : travelTypes) {
            String apiUrl = null;
            apiUrl = String.format(BASE_URL + "areaBasedList1?serviceKey=%s" +
                    "&numOfRows=100&pageNo=75&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&" +
                    "contentTypeId=%d", apikey, travelType.getId());
            System.out.println(apiUrl);
            JsonNode items = null;
            if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {
                LastCallApiData lastCallApiData = new LastCallApiData();
                lastCallApiData.setUrl(apiUrl);
                lastCallApiDataRepository.save(lastCallApiData);
                items = dataService.fetchApiData(apiUrl);
                if (items != null){

                    for (JsonNode item : items) {
                        String contentidCheck = item.get("contentid").asText();
                        Long areacodeCheck = item.get("areacode").asLong();
                        Long sigunguCheck = item.get("sigungucode").asLong();
                        String cat3Check = item.get("cat3").asText();
                        TravelPost travelPost = new TravelPost();
                        JsonNode items2 = null;
                        if (travelPostRepository.findByContentIdAndType(contentidCheck, travelType) == null) {
                            Areacode areacode = areacodeRepository.findByAreaCode(areacodeCheck);
                            travelPost.setAreacode(areacode);
                            travelPost.setLastCallApiData(lastCallApiData);
                            travelPost.setTravelType(travelType);
                            travelPost.setTitle(item.get("title").asText());
                            travelPost.setAddr1(item.get("addr1").asText());
                            travelPost.setAddr2(item.get("addr2").asText());
                            travelPost.setContentid(item.get("contentid").asText());
                            travelPost.setFirstimage(item.get("firstimage").asText());
                            travelPost.setFirstimage2(item.get("firstimage2").asText());
                            travelPost.setCpyrhtDivCd(item.get("cpyrhtDivCd").asText());
                            travelPost.setMapx(item.get("mapx").asDouble());
                            travelPost.setMapy(item.get("mapy").asDouble());
                            travelPost.setModifiedtime(item.get("modifiedtime").asText());
                            travelPost.setTel(item.get("tel").asText());

                            // 공통정보 api 호출 준비
                            apiUrl = String.format(BASE_URL + "detailCommon1?serviceKey=%s" +
                                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=N&areacodeYN=N&catcodeYN=N&addrinfoYN=N&mapinfoYN=N&overviewYN=Y&numOfRows=10&pageNo=1&" +
                                    "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                            if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {
                                LastCallApiData lastCallApiData1 = new LastCallApiData();
                                lastCallApiData1.setUrl(apiUrl);
                                items2 = dataService.fetchApiData(apiUrl);
                                System.out.println("공통정보 api 호출 완료");
                                for (JsonNode item2 : items2) {
                                    travelPost.setHomepage(item2.get("homepage").asText());
                                    travelPost.setOverview(item2.get("overview").asText());
                                }
                                lastCallApiDataRepository.save(lastCallApiData1);
                            }

                            // 소개정보 api 호출 준비
                            apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
                                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                                    "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                            if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {
                                LastCallApiData lastCallApiData1 = new LastCallApiData();
                                lastCallApiData1.setUrl(apiUrl);
                                items2 = dataService.fetchApiData(apiUrl);
                                System.out.println("소개정보 api 호출 완료");
                                if (travelType.getId() == 12) {
                                    for (JsonNode item2 : items2) {
                                        travelPost.setInfocenter(item2.get("infocenter").asText());
                                        travelPost.setParking(item2.get("parking").asText());
                                        travelPost.setRestdate(item2.get("restdate").asText());
                                        travelPost.setUsetime(item2.get("usetime").asText());
                                        travelPost.setUsetimefestival(null);
                                        travelPost.setEventplace(null);
                                        travelPost.setPlaytime(null);
                                        travelPost.setEventstartdate(null);
                                        travelPost.setEventenddate(null);
                                    }
                                } else if (travelType.getId() == 15) {
                                    for (JsonNode item2 : items2) {
                                        travelPost.setUsetimefestival(item2.get("usetimefestival").asText());
                                        travelPost.setEventplace(item2.get("eventplace").asText());
                                        travelPost.setPlaytime(item2.get("playtime").asText());
                                        travelPost.setEventstartdate(item2.get("eventstartdate").asText());
                                        travelPost.setEventenddate(item2.get("eventenddate").asText());
                                        travelPost.setInfocenter(null);
                                        travelPost.setParking(null);
                                        travelPost.setRestdate(null);
                                        travelPost.setUsetime(null);
                                    }
                                }
                                lastCallApiDataRepository.save(lastCallApiData1);
                            }

                            if (travelType.getId() == 15) {
                                // 반복정보 api 호출 준비
                                apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
                                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                                        "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                                if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {
                                    LastCallApiData lastCallApiData1 = new LastCallApiData();
                                    lastCallApiData1.setUrl(apiUrl);
                                    items2 = dataService.fetchApiData(apiUrl);
                                    if (items2 != null) {
                                        for (JsonNode item2 : items2) {
                                            Integer fldgubunCheck = item2.get("fldgubun").asInt();
                                            // 행사내용만 저장
                                            if (fldgubunCheck == 1) {
                                                travelPost.setInfoname(item2.get("infoname").asText());
                                                travelPost.setInfotext(item2.get("infotext").asText());
                                            }
                                        }
                                        System.out.println("반복정보 api 호출 완료");
                                        lastCallApiDataRepository.save(lastCallApiData1);
                                    } else {
                                        travelPost.setInfoname(null);
                                        travelPost.setInfotext(null);
                                    }
                                }
                            }

                            travelPostRepository.save(travelPost);
                            System.out.println("item 저장완료");

                        } else {
                            System.out.println("이미 저장되어 있음.");
                        }// end if문...
                    }// end item

                }//end items if문

            }//end findByUrl if문,..
        }// end travelTypes
    }// end saveTravelPosts
}

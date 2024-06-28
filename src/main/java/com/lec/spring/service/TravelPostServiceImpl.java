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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
        int count = 0;

        for (TravelType travelType : travelTypes) {
            String apiUrl = null;
            apiUrl = String.format(BASE_URL + "areaBasedList1?serviceKey=%s" +
                    "&numOfRows=14000&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&" +
                    "contentTypeId=%d", apikey, travelType.getId()); // TODO pageNo=12 완료
            System.out.println(apiUrl);
            JsonNode items = null;




            items = dataService.fetchApiData(apiUrl);

            if (items != null) {

                for (JsonNode item : items) {

                    try {

                        travelPostTransacionService.itemSave(item, travelType, detailInfo1, detailCommon1, detailIntro1);


                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("contentid : " + item.get("contentid").asText());
                        System.out.println("contenttypeid" + travelType.getId());
                        System.out.println("item 저장 실패");
                    }

                }// end item
            }//end items if문
        }// end travelTypes
    }// end saveTravelPosts

    @Override
    public void updateApiErrorTravelPosts() throws IOException, URISyntaxException {
        List<TravelType> travelTypeList = travelTypeRepository.findAll();
        int count = 0;

        for (TravelType travelType : travelTypeList) {

            List<TravelClassDetail> travelClassDetailList = travelClassDetailRepository.findByTravelTypeList(travelType);

            List<TravelPost> travelPostList = travelPostRepository.findByOverviewIsNull(travelClassDetailList);

            // api 에러 발생시 contentid / traveltype id 확인후 기입하고 사용할것.
//            if (travelType.getId() ==12){
//                TravelPost travelPost = travelPostRepository.findPostByContentId("1689909");
//
//                String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/" +
//                        "detailCommon1?serviceKey=%s" +
//                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1" +
//                        "&contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelType.getId());
//
//                JsonNode items = null;
//
//                try {
//                    items = dataService.fetchApiData(apiUrl);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//
//                for (JsonNode item : items) {
//                    Long areacodeCheck = item.get("areacode").asLong();
//                    Long sigunguCheck = item.get("sigungucode").asLong();
//                    String cat3Check = item.get("cat3").asText();
//
//                    TravelClassDetail travelClassDetail = travelClassDetailRepository.findTravelTypeIdByCode(travelType, cat3Check);
//
//                    Areacode areacode = areacodeRepository.findByAreaCode(areacodeCheck);
//                    Sigungucode sigungucode = sigungucodeRepository.findAreacodeBySigungucode(areacode, sigunguCheck);
//
//                    travelPost.setSigungucode(sigungucode);
//                    travelPost.setTravelClassDetail(travelClassDetail);
//                    travelPost.setTitle(item.get("title").asText());
//                    travelPost.setAddr1(item.get("addr1").asText());
//                    travelPost.setAddr2(item.get("addr2").asText());
//                    travelPost.setContentid(item.get("contentid").asText());
//                    travelPost.setFirstimage(item.get("firstimage").asText());
//                    travelPost.setFirstimage2(item.get("firstimage2").asText());
//                    travelPost.setCpyrhtDivCd(item.get("cpyrhtDivCd").asText());
//                    travelPost.setMapx(item.get("mapx").asDouble());
//                    travelPost.setMapy(item.get("mapy").asDouble());
//                    travelPost.setModifiedtime(item.get("modifiedtime").asText());
//                    travelPost.setTel(item.get("tel").asText());
//                    travelPost.setHomepage(item.get("homepage").asText().isEmpty() ? null : item.get("homepage").asText());
//                    travelPost.setOverview(item.get("overview").asText().isEmpty() ? null : item.get("overview").asText());
//
//                }
//
//                LocalDate today = LocalDate.now();
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                String formattedDate = today.format(formatter);
//
//                LastCallApiDate lastCallApiDate = new LastCallApiDate();
//                lastCallApiDate.setUrl(apiUrl + formattedDate);
//                lastCallApiDataRepository.save(lastCallApiDate);
//
//                // 소개정보 api
//                apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
//                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
//                        "contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelType.getId());
//
//                try {
//                    items = dataService.fetchApiData(apiUrl);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    break;
//                }
//
//                if (items != null) {
//                    if (travelType.getId() == 12) {
//                        for (JsonNode item : items) {
//                            travelPost.setInfocenter(item.get("infocenter").asText().isEmpty() ? null : item.get("infocenter").asText());
//                            travelPost.setParking(item.get("parking").asText().isEmpty() ? null : item.get("parking").asText());
//                            travelPost.setRestdate(item.get("restdate").asText().isEmpty() ? null : item.get("restdate").asText());
//                            travelPost.setUsetime(item.get("usetime").asText().isEmpty() ? null : item.get("usetime").asText());
//                            travelPost.setUsetimefestival(null);
//                            travelPost.setEventplace(null);
//                            travelPost.setPlaytime(null);
//                            travelPost.setEventstartdate(null);
//                            travelPost.setEventenddate(null);
//                        }
//                    } else if (travelType.getId() == 15) {
//                        for (JsonNode item : items) {
//                            travelPost.setUsetimefestival(item.get("usetimefestival").asText().isEmpty() ? null : item.get("usetimefestival").asText());
//                            travelPost.setEventplace(item.get("eventplace").asText().isEmpty() ? null : item.get("eventplace").asText());
//                            travelPost.setPlaytime(item.get("playtime").asText().isEmpty() ? null : item.get("playtime").asText());
//                            travelPost.setEventstartdate(item.get("eventstartdate").asText().isEmpty() ? null : item.get("eventstartdate").asText());
//                            travelPost.setEventenddate(item.get("eventenddate").asText().isEmpty() ? null : item.get("eventenddate").asText());
//                            travelPost.setInfocenter(null);
//                            travelPost.setParking(null);
//                            travelPost.setRestdate(null);
//                            travelPost.setUsetime(null);
//                        }
//                    }
//                }
//
//                // 반복정보 api 호출
//                if (travelType.getId() == 15) {
//
//                    apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
//                            "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
//                            "contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelType.getId());
//
//                    try {
//                        items = dataService.fetchApiData(apiUrl);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        break;
//                    }
//
//                    if (items != null) {
//                        for (JsonNode item : items) {
//                            int fldgubunCheck = item.get("fldgubun").asInt();
//                            // 행사내용만 저장
//                            if (fldgubunCheck == 1) {
//                                travelPost.setInfoname(item.get("infoname").asText().isEmpty() ? null : item.get("infoname").asText());
//                                travelPost.setInfotext(item.get("infotext").asText().isEmpty() ? null : item.get("infoname").asText());
//                            }
//                        }
//                        System.out.println("반복정보 api 호출 완료");
//                    }
//
//                } else {
//                    // Contentid ==  15 가 아닐경우.
//                    travelPost.setInfoname(null);
//                    travelPost.setInfotext(null);
//                }
//                travelPost.setLastCallApiData(lastCallApiDate);
//                travelPostRepository.update(travelPost);
//                System.out.println("item update 완료");
//
//            }

            for (TravelPost travelPost : travelPostList) {
                // 공통정보 api
                String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/" +
                        "detailCommon1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1" +
                        "&contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelType.getId());

                JsonNode items = null;

                try {
                    items = dataService.fetchApiData(apiUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

                if(items != null){

                    for (JsonNode item : items) {
                        Long areacodeCheck = item.get("areacode").asLong();
                        Long sigunguCheck = item.get("sigungucode").asLong();
                        String cat3Check = item.get("cat3").asText();

                        TravelClassDetail travelClassDetail = travelClassDetailRepository.findTravelTypeIdByCode(travelType, cat3Check);

                        Areacode areacode = areacodeRepository.findByAreaCode(areacodeCheck);
                        Sigungucode sigungucode = sigungucodeRepository.findAreacodeBySigungucode(areacode, sigunguCheck);

                        travelPost.setSigungucode(sigungucode);
                        travelPost.setTravelClassDetail(travelClassDetail);
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
                        travelPost.setHomepage(item.get("homepage").asText().isEmpty() ? null : item.get("homepage").asText());
                        travelPost.setOverview(item.get("overview").asText().isEmpty() ? null : item.get("overview").asText());

                    }

                    LocalDate today = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = today.format(formatter);

                    LastCallApiDate lastCallApiDate = new LastCallApiDate();
                    lastCallApiDate.setUrl(apiUrl + formattedDate);
                    lastCallApiDataRepository.save(lastCallApiDate);

                    // 소개정보 api
                    apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
                            "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                            "contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelType.getId());

                    try {
                        items = dataService.fetchApiData(apiUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    if (items != null) {
                        if (travelType.getId() == 12) {
                            for (JsonNode item : items) {
                                travelPost.setInfocenter(item.get("infocenter").asText().isEmpty() ? null : item.get("infocenter").asText());
                                travelPost.setParking(item.get("parking").asText().isEmpty() ? null : item.get("parking").asText());
                                travelPost.setRestdate(item.get("restdate").asText().isEmpty() ? null : item.get("restdate").asText());
                                travelPost.setUsetime(item.get("usetime").asText().isEmpty() ? null : item.get("usetime").asText());
                                travelPost.setUsetimefestival(null);
                                travelPost.setEventplace(null);
                                travelPost.setPlaytime(null);
                                travelPost.setEventstartdate(null);
                                travelPost.setEventenddate(null);
                            }
                        } else if (travelType.getId() == 15) {
                            for (JsonNode item : items) {
                                travelPost.setUsetimefestival(item.get("usetimefestival").asText().isEmpty() ? null : item.get("usetimefestival").asText());
                                travelPost.setEventplace(item.get("eventplace").asText().isEmpty() ? null : item.get("eventplace").asText());
                                travelPost.setPlaytime(item.get("playtime").asText().isEmpty() ? null : item.get("playtime").asText());
                                travelPost.setEventstartdate(item.get("eventstartdate").asText().isEmpty() ? null : item.get("eventstartdate").asText());
                                travelPost.setEventenddate(item.get("eventenddate").asText().isEmpty() ? null : item.get("eventenddate").asText());
                                travelPost.setInfocenter(null);
                                travelPost.setParking(null);
                                travelPost.setRestdate(null);
                                travelPost.setUsetime(null);
                            }
                        }
                    }

                    // 반복정보 api 호출
                    if (travelType.getId() == 15) {

                        apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
                                "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                                "contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelType.getId());

                        try {
                            items = dataService.fetchApiData(apiUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }

                        if (items != null) {
                            for (JsonNode item : items) {
                                int fldgubunCheck = item.get("fldgubun").asInt();
                                // 행사내용만 저장
                                if (fldgubunCheck == 1) {
                                    travelPost.setInfoname(item.get("infoname").asText().isEmpty() ? null : item.get("infoname").asText());
                                    travelPost.setInfotext(item.get("infotext").asText().isEmpty() ? null : item.get("infoname").asText());
                                }
                            }
                            System.out.println("반복정보 api 호출 완료");
                        }

                    } else {
                        // Contentid ==  15 가 아닐경우.
                        travelPost.setInfoname(null);
                        travelPost.setInfotext(null);
                    }
                    travelPost.setLastCallApiDate(lastCallApiDate);
                    travelPostRepository.update(travelPost);
                    System.out.println("item update 완료");

                    count++;
                    if (count == 1000) {
                        // 트래픽 허용량
                        break;
                    }

                }else {
                    System.out.println("공통데이터 없음");
                    travelPostRepository.delete(travelPost);
                    System.out.println("삭제 완료");
                }

            }

            if (count == 1000) {
                // 트래픽 허용량
                break;
            }

        }

    }

    @Override
    public TravelPost update(TravelPost travelPost) {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/" +
                "detailCommon1?serviceKey=%s" +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1" +
                "&contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelPost.getTravelClassDetail().getTravelType().getId());

        JsonNode items = null;

        if(lastCallApiDataRepository.findByUrlAndRegDate(apiUrl,formattedDate) == null){

            try {
                items = dataService.fetchApiData(apiUrl);
            } catch (Exception e) {
                e.printStackTrace();

            }

            for (JsonNode item : items) {
                Long areacodeCheck = item.get("areacode").asLong();
                Long sigunguCheck = item.get("sigungucode").asLong();
                String cat3Check = item.get("cat3").asText();

                TravelClassDetail travelClassDetail = travelClassDetailRepository.findTravelTypeIdByCode(travelPost.getTravelClassDetail().getTravelType(), cat3Check);

                Areacode areacode = areacodeRepository.findByAreaCode(areacodeCheck);
                Sigungucode sigungucode = sigungucodeRepository.findAreacodeBySigungucode(areacode, sigunguCheck);

                travelPost.setSigungucode(sigungucode);
                travelPost.setTravelClassDetail(travelClassDetail);
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
                travelPost.setHomepage(item.get("homepage").asText().isEmpty() ? null : item.get("homepage").asText());
                travelPost.setOverview(item.get("overview").asText().isEmpty() ? null : item.get("overview").asText());

            }



            LastCallApiDate lastCallApiDate = new LastCallApiDate();
            lastCallApiDate.setUrl(apiUrl);
            System.out.println(apiUrl);
            lastCallApiDataRepository.save(lastCallApiDate);

            // 소개정보 api
            apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                    "contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelPost.getTravelClassDetail().getTravelType().getId());

            try {
                items = dataService.fetchApiData(apiUrl);
            } catch (Exception e) {
                e.printStackTrace();

            }

            if (items != null) {
                if (travelPost.getTravelClassDetail().getTravelType().getId() == 12) {
                    for (JsonNode item : items) {
                        travelPost.setInfocenter(item.get("infocenter").asText().isEmpty() ? null : item.get("infocenter").asText());
                        travelPost.setParking(item.get("parking").asText().isEmpty() ? null : item.get("parking").asText());
                        travelPost.setRestdate(item.get("restdate").asText().isEmpty() ? null : item.get("restdate").asText());
                        travelPost.setUsetime(item.get("usetime").asText().isEmpty() ? null : item.get("usetime").asText());
                        travelPost.setUsetimefestival(null);
                        travelPost.setEventplace(null);
                        travelPost.setPlaytime(null);
                        travelPost.setEventstartdate(null);
                        travelPost.setEventenddate(null);
                    }
                } else if (travelPost.getTravelClassDetail().getTravelType().getId() == 15) {
                    for (JsonNode item : items) {
                        travelPost.setUsetimefestival(item.get("usetimefestival").asText().isEmpty() ? null : item.get("usetimefestival").asText());
                        travelPost.setEventplace(item.get("eventplace").asText().isEmpty() ? null : item.get("eventplace").asText());
                        travelPost.setPlaytime(item.get("playtime").asText().isEmpty() ? null : item.get("playtime").asText());
                        travelPost.setEventstartdate(item.get("eventstartdate").asText().isEmpty() ? null : item.get("eventstartdate").asText());
                        travelPost.setEventenddate(item.get("eventenddate").asText().isEmpty() ? null : item.get("eventenddate").asText());
                        travelPost.setInfocenter(null);
                        travelPost.setParking(null);
                        travelPost.setRestdate(null);
                        travelPost.setUsetime(null);
                    }
                }
            }

            // 반복정보 api 호출
            if (travelPost.getTravelClassDetail().getTravelType().getId() == 15) {

                apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                        "contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelPost.getTravelClassDetail().getTravelType().getId());

                try {
                    items = dataService.fetchApiData(apiUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (items != null) {
                    for (JsonNode item : items) {
                        int fldgubunCheck = item.get("fldgubun").asInt();
                        // 행사내용만 저장
                        if (fldgubunCheck == 1) {
                            travelPost.setInfoname(item.get("infoname").asText().isEmpty() ? null : item.get("infoname").asText());
                            travelPost.setInfotext(item.get("infotext").asText().isEmpty() ? null : item.get("infoname").asText());
                        }
                    }
                    System.out.println("반복정보 api 호출 완료");
                }

            } else {
                // Contentid ==  15 가 아닐경우.
                travelPost.setInfoname(null);
                travelPost.setInfotext(null);
            }
            travelPost.setLastCallApiDate(lastCallApiDate);
            travelPostRepository.update(travelPost);
            System.out.println("item update 완료");

        }else {
            System.out.println(formattedDate + " 업데이트 진행했음.");
        }

        return travelPost;

    }

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
    public List<TravelPost> selectedByOverviewIsNull(List<TravelClassDetail> travelClassDetailList) {
        return travelPostRepository.findByOverviewIsNull(travelClassDetailList);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeTotalList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList, long travelTypeId) {
        return travelPostRepository.findTravelTypeByAreacodeTotalList(travelClassDetailList, sigungucodeList, travelTypeId);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList
            , long travelTypeId, int limit, int offset) {
        return travelPostRepository.findTravelTypeByAreacodeList(travelClassDetailList, sigungucodeList, travelTypeId, limit, offset);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSearchTotalList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList
            , long travelTypeId, String searchQuery) {
        return travelPostRepository.findTravelTypeByAreacodeAndSearchTotalList(travelClassDetailList, sigungucodeList, travelTypeId, searchQuery);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSearchList(List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList
            , long travelTypeId, String searchQuery, int limit, int offset) {
        return travelPostRepository.findTravelTypeByAreacodeAndSearchList(travelClassDetailList, sigungucodeList, travelTypeId, searchQuery, limit, offset);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeTotalList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode
            , long travelTypeId) {
        return travelPostRepository.findTravelTypeByAreacodeAndSigungucodeTotalList(travelClassDetailList, sigungucode, travelTypeId);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode, long travelTypeId
            , int limit, int offset) {
        return travelPostRepository.findTravelTypeByAreacodeAndSigungucodeList(travelClassDetailList, sigungucode, travelTypeId, limit, offset);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeAndSearchTotalList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode
            , long travelTypeId, String searchQuery) {
        return travelPostRepository.findTravelTypeByAreacodeAndSigungucodeAndSearchTotalList(travelClassDetailList, sigungucode, travelTypeId, searchQuery);
    }

    @Override
    public List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeAndSearchList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode
            , long travelTypeId, String searchQuery, int limit, int offset) {
        return travelPostRepository.findTravelTypeByAreacodeAndSigungucodeAndSearchList(travelClassDetailList, sigungucode, travelTypeId, searchQuery, limit, offset);
    }

    @Override
    public List<TravelPost> selectedByTravelTypeTotalList(List<TravelClassDetail> travelClassDetailList, long travelTypeId) {
        return travelPostRepository.findByTravelTypeTotalList(travelClassDetailList, travelTypeId);
    }

    @Override
    public List<TravelPost> selectedByTravelTypeList(List<TravelClassDetail> travelClassDetailList, long travelTypeId, int limit, int offset) {
        return travelPostRepository.findByTravelTypeList(travelClassDetailList, travelTypeId, limit, offset);
    }

    @Override
    public List<TravelPost> selectedByTravelTypAndSearchTotalList(List<TravelClassDetail> travelClassDetailList, long travelTypeId, String searchQuery) {
        return travelPostRepository.findByTravelTypAndSearchTotalList(travelClassDetailList, travelTypeId, searchQuery);
    }

    @Override
    public List<TravelPost> selectedByTravelTypAndSearchList(List<TravelClassDetail> travelClassDetailList, long travelTypeId, String searchQuery, int limit, int offset) {
        return travelPostRepository.findByTravelTypAndSearchList(travelClassDetailList, travelTypeId, searchQuery, limit, offset);
    }

    @Override
    public TravelPost getTravelPostById(String id) throws IOException {
        return travelPostRepository.findPostByContentId(id);
    }

    @Override
    public int saveLike(Long postId, Long userId) {
        return travelPostRepository.addTravelPostLike(postId, userId);
    }

    @Override
    public int deleteLike(Long postId, Long userId) {
        return travelPostRepository.deleteTravelPostLike(postId, userId);
    }

    @Override
    public Long selectedLike(Long postId, Long userId) {
        return travelPostRepository.findLike(postId, userId);
    }

    @Override
    public Long totalLike(Long postId) {
        return travelPostRepository.countLike(postId);
    }


}

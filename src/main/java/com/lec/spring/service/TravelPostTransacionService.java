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
import java.util.concurrent.TimeUnit;

@Service
public class TravelPostTransacionService {

    @Value("${app.apikey}")
    private String apikey;
    private TravelPostRepository travelPostRepository;
    private TravelClassDetailRepository travelClassDetailRepository;
    private LastCallApiDateRepository lastCallApiDataRepository;
    private AreacodeRepository areacodeRepository;
    private SigungucodeRepository sigungucodeRepository;
    private DataService dataService;
    static String BASE_URL = "https://apis.data.go.kr/B551011/KorService1/";

    @Autowired
    public TravelPostTransacionService(SqlSession sqlSession, DataService dataService) {
        travelPostRepository = sqlSession.getMapper(TravelPostRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
        travelClassDetailRepository = sqlSession.getMapper(TravelClassDetailRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        sigungucodeRepository = sqlSession.getMapper(SigungucodeRepository.class);
        this.dataService = dataService;

    }

    @Transactional
    public void itemSave(JsonNode item, TravelType travelType, LastCallApiDate detailInfo1,
                         LastCallApiDate detailCommon1, LastCallApiDate detailIntro1) throws IOException, URISyntaxException {

        String contentidCheck = item.get("contentid").asText();
        Long areacodeCheck = item.get("areacode").asLong();
        Long sigunguCheck = item.get("sigungucode").asLong();
        String cat3Check = item.get("cat3").asText();
        TravelPost travelPost = new TravelPost();
        JsonNode items2 = null;

        TravelClassDetail travelClassDetail = travelClassDetailRepository.findTravelTypeIdByCode(travelType, cat3Check);
        travelClassDetail.setTravelType(travelType);

        Areacode areacode = areacodeRepository.findByAreaCode(areacodeCheck);
        Sigungucode sigungucode = sigungucodeRepository.findAreacodeBySigungucode(areacode, sigunguCheck);
        sigungucode.setAreacode(areacode);

        try {

            if (travelPostRepository.findByContentIdAndType(contentidCheck, travelClassDetail) == null) {

                String apiUrl = null;
                // 지역기반 정보

                localInfo(travelPost, sigungucode, travelClassDetail, item);

                // 공통정보 api 호출 준비
                apiUrl = String.format(BASE_URL + "detailCommon1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=N&areacodeYN=N&catcodeYN=N&addrinfoYN=N&mapinfoYN=N&overviewYN=Y&numOfRows=10&pageNo=1&"
                        + "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {

                    detailCommon(detailCommon1, apiUrl, items2, travelPost);

                } else {
                    System.out.println("공통정보 이미호출 완료");
                }

                // 소개정보 api 호출 준비
                apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                        "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {

                    detailIntro(detailIntro1, apiUrl, items2, travelPost, travelType);

                } else {
                    System.out.println("소개정보 이미호출 완료");

                }

                if (travelType.getId() == 15) {
                    // 반복정보 api 호출 준비
                    apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
                            "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                            "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                    if (lastCallApiDataRepository.findByUrl(apiUrl) == null) {

                        detailInfo(detailInfo1, apiUrl, items2, travelPost);

                    }
                } else {

                    travelPost.setInfoname(null);
                    travelPost.setInfotext(null);
                }

                lastCallApiDataRepository.save(detailCommon1);
                lastCallApiDataRepository.save(detailIntro1);
                travelPost.setLastCallApiDate(detailIntro1);
                travelPostRepository.save(travelPost);
                System.out.println("item 저장완료");

            } else {
//                System.out.println("이미 저장되어 있음.");
            }// end if문...

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("if 문 저장 실패");
            throw e;
        }


    }

    @Transactional
    public void itemSaveAndUpdate(JsonNode item, TravelType travelType) throws IOException, URISyntaxException {

        String contentidCheck = item.get("contentid").asText();
        Long areacodeCheck = item.get("areacode").asLong();
        Long sigunguCheck = item.get("sigungucode").asLong();
        String cat3Check = item.get("cat3").asText();
        TravelPost travelPost = new TravelPost();
        JsonNode items2 = null;
        LastCallApiDate lastCallApiDate = new LastCallApiDate();

        TravelClassDetail travelClassDetail = travelClassDetailRepository.findTravelTypeIdByCode(travelType, cat3Check);
        travelClassDetail.setTravelType(travelType);

        Areacode areacode = areacodeRepository.findByAreaCode(areacodeCheck);
        Sigungucode sigungucode = sigungucodeRepository.findAreacodeBySigungucode(areacode, sigunguCheck);
        sigungucode.setAreacode(areacode);

        try {

            if (travelPostRepository.findByContentIdAndType(contentidCheck, travelClassDetail) == null) {

                String apiUrl = null;
                // 지역기반 정보

                localInfo(travelPost, sigungucode, travelClassDetail, item);

                // 공통정보 api 호출 준비
                apiUrl = String.format(BASE_URL + "detailCommon1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=N&areacodeYN=N&catcodeYN=N&addrinfoYN=N&mapinfoYN=N&overviewYN=Y&numOfRows=10&pageNo=1&"
                        + "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                detailCommon(apiUrl, items2, travelPost);

                // 소개정보 api 호출 준비
                apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                        "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                detailIntro(apiUrl, items2, travelPost, travelType);


                if (travelType.getId() == 15) {
                    // 반복정보 api 호출 준비
                    apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
                            "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                            "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                    detailInfo(apiUrl, items2, travelPost);

                } else {

                    travelPost.setInfoname(null);
                    travelPost.setInfotext(null);
                }

                lastCallApiDate.setUrl(apiUrl);
                lastCallApiDataRepository.save(lastCallApiDate);

                travelPost.setLastCallApiDate(lastCallApiDate);
                travelPostRepository.save(travelPost);
                System.out.println("item 저장완료");

            } else {

                String apiUrl = null;
                // 지역기반 정보

                localInfo(travelPost, sigungucode, travelClassDetail, item);

                // 공통정보 api 호출 준비
                apiUrl = String.format(BASE_URL + "detailCommon1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=N&areacodeYN=N&catcodeYN=N&addrinfoYN=N&mapinfoYN=N&overviewYN=Y&numOfRows=10&pageNo=1&"
                        + "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                detailCommon(apiUrl, items2, travelPost);

                // 소개정보 api 호출 준비
                apiUrl = String.format(BASE_URL + "detailIntro1?serviceKey=%s" +
                        "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                        "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                detailIntro(apiUrl, items2, travelPost, travelType);


                if (travelType.getId() == 15) {
                    // 반복정보 api 호출 준비
                    apiUrl = String.format(BASE_URL + "detailInfo1?serviceKey=%s" +
                            "&MobileOS=ETC&MobileApp=AppTest&_type=json&numOfRows=10&pageNo=1&" +
                            "contentId=%s&contentTypeId=%d", apikey, contentidCheck, travelType.getId());

                    detailInfo(apiUrl, items2, travelPost);

                } else {

                    travelPost.setInfoname(null);
                    travelPost.setInfotext(null);
                }

                lastCallApiDate.setUrl(apiUrl);
                lastCallApiDataRepository.save(lastCallApiDate);

                travelPost.setLastCallApiDate(lastCallApiDate);
                travelPostRepository.update(travelPost);

                System.out.println("item 업데이트 완료");
            }// end if문...

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @Transactional
    public void localInfo(TravelPost travelPost, Sigungucode sigungucode, TravelClassDetail travelClassDetail, JsonNode item) {
        travelPost.setSigungucode(sigungucode);
        travelPost.setTravelClassDetail(travelClassDetail);
        travelPost.setTitle(item.get("title").asText().isEmpty() ? null : item.get("title").asText());
        travelPost.setAddr1(item.get("addr1").asText().isEmpty() ? null : item.get("addr1").asText());
        travelPost.setAddr2(item.get("addr2").asText().isEmpty() ? null : item.get("addr2").asText());
        travelPost.setContentid(item.get("contentid").asText().isEmpty() ? null : item.get("contentid").asText());
        travelPost.setFirstimage(item.get("firstimage").asText().isEmpty() ? null : item.get("firstimage").asText());
        travelPost.setFirstimage2(item.get("firstimage2").asText().isEmpty() ? null : item.get("firstimage2").asText());
        travelPost.setCpyrhtDivCd(item.get("cpyrhtDivCd").asText().isEmpty() ? null : item.get("cpyrhtDivCd").asText());
        travelPost.setMapx(item.get("mapx").asText().isEmpty() ? null : item.get("mapx").asDouble());
        travelPost.setMapy(item.get("mapy").asText().isEmpty() ? null : item.get("mapy").asDouble());
        travelPost.setModifiedtime(item.get("modifiedtime").asText().isEmpty() ? null : item.get("modifiedtime").asText());
        travelPost.setTel(item.get("tel").asText().isEmpty() ? null : item.get("tel").asText());
    }

    @Transactional
    public void detailCommon(LastCallApiDate detailCommon1, String apiUrl, JsonNode items2, TravelPost travelPost) throws IOException, URISyntaxException {

        detailCommon1.setUrl(apiUrl);
        try {
            items2 = dataService.fetchApiData(apiUrl);


            if (items2 != null) {
                for (JsonNode item2 : items2) {
                    travelPost.setHomepage(item2.get("homepage").asText().isEmpty() ? null : item2.get("homepage").asText());
                    travelPost.setOverview(item2.get("overview").asText().isEmpty() ? null : item2.get("overview").asText());
                }
            }

            System.out.println("공통정보 api 호출 완료");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @Transactional
    public void detailCommon(String apiUrl, JsonNode items2, TravelPost travelPost) throws IOException, URISyntaxException {

        try {
            items2 = dataService.fetchApiData(apiUrl);


            if (items2 != null) {
                for (JsonNode item2 : items2) {
                    travelPost.setHomepage(item2.get("homepage").asText().isEmpty() ? null : item2.get("homepage").asText());
                    travelPost.setOverview(item2.get("overview").asText().isEmpty() ? null : item2.get("overview").asText());
                }
            }

            System.out.println("공통정보 api 호출 완료");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @Transactional
    public void detailIntro(LastCallApiDate detailIntro1, String apiUrl, JsonNode items2,
                            TravelPost travelPost, TravelType travelType) throws IOException, URISyntaxException {

        detailIntro1.setUrl(apiUrl);
        try {

            items2 = dataService.fetchApiData(apiUrl);

            if (items2 != null) {
                if (travelType.getId() == 12) {
                    for (JsonNode item2 : items2) {
                        travelPost.setInfocenter(item2.get("infocenter").asText().isEmpty() ? null : item2.get("infocenter").asText());
                        travelPost.setParking(item2.get("parking").asText().isEmpty() ? null : item2.get("parking").asText());
                        travelPost.setRestdate(item2.get("restdate").asText().isEmpty() ? null : item2.get("restdate").asText());
                        travelPost.setUsetime(item2.get("usetime").asText().isEmpty() ? null : item2.get("usetime").asText());
                        travelPost.setUsetimefestival(null);
                        travelPost.setEventplace(null);
                        travelPost.setPlaytime(null);
                        travelPost.setEventstartdate(null);
                        travelPost.setEventenddate(null);
                    }
                } else if (travelType.getId() == 15) {
                    for (JsonNode item2 : items2) {
                        travelPost.setUsetimefestival(item2.get("usetimefestival").asText().isEmpty() ? null : item2.get("usetimefestival").asText());
                        travelPost.setEventplace(item2.get("eventplace").asText().isEmpty() ? null : item2.get("eventplace").asText());
                        travelPost.setPlaytime(item2.get("playtime").asText().isEmpty() ? null : item2.get("playtime").asText());
                        travelPost.setEventstartdate(item2.get("eventstartdate").asText().isEmpty() ? null : item2.get("eventstartdate").asText());
                        travelPost.setEventenddate(item2.get("eventenddate").asText().isEmpty() ? null : item2.get("eventenddate").asText());
                        travelPost.setInfocenter(null);
                        travelPost.setParking(null);
                        travelPost.setRestdate(null);
                        travelPost.setUsetime(null);
                    }
                }

                System.out.println("소개정보 api 호출 완료");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @Transactional
    public void detailIntro(String apiUrl, JsonNode items2, TravelPost travelPost, TravelType travelType) throws IOException, URISyntaxException {

        try {

            items2 = dataService.fetchApiData(apiUrl);


            if (items2 != null) {
                if (travelType.getId() == 12) {
                    for (JsonNode item2 : items2) {
                        travelPost.setInfocenter(item2.get("infocenter").asText().isEmpty() ? null : item2.get("infocenter").asText());
                        travelPost.setParking(item2.get("parking").asText().isEmpty() ? null : item2.get("parking").asText());
                        travelPost.setRestdate(item2.get("restdate").asText().isEmpty() ? null : item2.get("restdate").asText());
                        travelPost.setUsetime(item2.get("usetime").asText().isEmpty() ? null : item2.get("usetime").asText());
                        travelPost.setUsetimefestival(null);
                        travelPost.setEventplace(null);
                        travelPost.setPlaytime(null);
                        travelPost.setEventstartdate(null);
                        travelPost.setEventenddate(null);
                    }
                } else if (travelType.getId() == 15) {
                    for (JsonNode item2 : items2) {
                        travelPost.setUsetimefestival(item2.get("usetimefestival").asText().isEmpty() ? null : item2.get("usetimefestival").asText());
                        travelPost.setEventplace(item2.get("eventplace").asText().isEmpty() ? null : item2.get("eventplace").asText());
                        travelPost.setPlaytime(item2.get("playtime").asText().isEmpty() ? null : item2.get("playtime").asText());
                        travelPost.setEventstartdate(item2.get("eventstartdate").asText().isEmpty() ? null : item2.get("eventstartdate").asText());
                        travelPost.setEventenddate(item2.get("eventenddate").asText().isEmpty() ? null : item2.get("eventenddate").asText());
                        travelPost.setInfocenter(null);
                        travelPost.setParking(null);
                        travelPost.setRestdate(null);
                        travelPost.setUsetime(null);
                    }
                }
            }

            System.out.println("소개정보 api 호출 완료");

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @Transactional
    public void detailInfo(LastCallApiDate detailInfo1, String apiUrl, JsonNode items2, TravelPost travelPost)
            throws IOException, URISyntaxException {

        detailInfo1.setUrl(apiUrl);

        try {

            items2 = dataService.fetchApiData(apiUrl);

            if (items2 != null) {
                for (JsonNode item2 : items2) {
                    String infonameCheck = item2.get("infoname").asText();
                    // 행사내용만 저장
                    if (infonameCheck.equals("행사내용")) {
                        travelPost.setInfoname(item2.get("infoname").asText().isEmpty() ? null : item2.get("infoname").asText());
                        travelPost.setInfotext(item2.get("infotext").asText().isEmpty() ? null : item2.get("infotext").asText());
                    }
                }
                System.out.println("반복정보 api 호출 완료");
                lastCallApiDataRepository.save(detailInfo1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }

    @Transactional
    public void detailInfo(String apiUrl, JsonNode items2, TravelPost travelPost) throws IOException, URISyntaxException {

        try {

            items2 = dataService.fetchApiData(apiUrl);

            if (items2 != null) {
                for (JsonNode item2 : items2) {
                    String infonameCheck = item2.get("infoname").asText();
                    // 행사내용만 저장
                    if (infonameCheck.equals("행사내용")) {
                        travelPost.setInfoname(item2.get("infoname").asText().isEmpty() ? null : item2.get("infoname").asText());
                        travelPost.setInfotext(item2.get("infotext").asText().isEmpty() ? null : item2.get("infotext").asText());
                    }
                }
                System.out.println("반복정보 api 호출 완료");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


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

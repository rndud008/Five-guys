package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Sigungucode;
import com.lec.spring.repository.AreacodeRepository;
import com.lec.spring.repository.SigungucodeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SigungucodeServiceImpl implements SigungucodeService {
    @Value("${app.apikey}")
    private String apikey;
    private SigungucodeRepository sigungucodeRepository;
    private AreacodeRepository areacodeRepository;
    private DataService dataService;

    @Autowired
    public SigungucodeServiceImpl(SqlSession sqlSession, DataService dataService) {
        sigungucodeRepository = sqlSession.getMapper(SigungucodeRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        this.dataService = dataService;
    }

    public void saveSigungucodes() throws IOException {
        List<Areacode> areacodes = areacodeRepository.findAll();

        for (Areacode areacode : areacodes) {

            String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/areaCode1?" +
                    "serviceKey=%s&numOfRows=30&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=%d&_type=json", apikey, areacode.getAreacode()).trim();

            System.out.println(apiUrl);

            JsonNode items = null;
            try {
                items = dataService.fetchApiData(apiUrl);

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            if (items != null) {

                for (JsonNode item : items) {
                    Long sigunguCheck = item.get("code").asLong();
                    if (sigungucodeRepository.findAreacodeBySigungucode(areacode, sigunguCheck) == null){
                        itemSave(item, areacode);
                    }else {
                        System.out.println("이미 저장되어있음.");
                    }

                }
            } else {
                System.err.println("Failed to fetch data from API for areacode: " + areacode.getAreacode());
            }

            // API 호출 간격을 두기 위해 잠시 대기
            timeUnit();

        }
    }

    public void itemSave(JsonNode item, Areacode areacode){
        Sigungucode sigungucode = new Sigungucode();
        sigungucode.setAreacode(areacode);
        sigungucode.setSigungucode(item.get("code").asLong());
        sigungucode.setName(item.get("name").asText());
        sigungucodeRepository.save(sigungucode);

        System.out.println("저장완료");
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

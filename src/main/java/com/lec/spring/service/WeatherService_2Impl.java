package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.domain.WeatherDTO_2;
import com.lec.spring.repository.AreacodeRepository;
import com.lec.spring.repository.LastCallApiDateRepository;
import com.lec.spring.repository.WeatherRepository_2;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@Service
public class WeatherService_2Impl implements WeatherService_2 {

    @Value("${app.apikey}")
    private String apiKey;
    private String baseUrl_1 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
    private String baseUrl_2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
    private String numOfRows = "10";

    private WeatherRepository_2 weatherRepository_2;
    private AreacodeRepository areacodeRepository;
    private LastCallApiDateRepository lastCallApiDateRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'0600'");
    String tmFc = dateFormat.format(new Date());
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    String tmFc1 = dateFormat1.format(new Date());
    @Autowired
    public WeatherService_2Impl(SqlSession sqlSession) {
        weatherRepository_2 = sqlSession.getMapper(WeatherRepository_2.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        lastCallApiDateRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
    }

    @Override
    @Transactional
    public void saveWeatherInfo_middle(Long areacode, String regId) {
        String url =
                baseUrl_1 +
                        "?serviceKey=" + apiKey +
                        "&pageNo=1&numOfRows=" + numOfRows +
                        "&dataType=JSON&regId=" + regId +
                        "&tmFc=" + tmFc;
        System.out.println("중기날씨(4 ~ 7일) 정보[기온] 요청 URL: " + url);

        try {
            LastCallApiDate existingData = lastCallApiDateRepository.findByUrl(url);

            if (existingData == null) {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                BufferedReader rd;
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonObject = mapper.readTree(sb.toString());

                JsonNode response = jsonObject.get("response");
                JsonNode body = response.get("body");
                JsonNode items = body.get("items");
                JsonNode itemArray = items.get("item");
                // 마지막 호출 데이터 저장
                LastCallApiDate lastCallApiDate = new LastCallApiDate();
                lastCallApiDate.setUrl(url);
                lastCallApiDateRepository.save(lastCallApiDate);

                // areacode로 Areacode 객체 가져오기
                Areacode areaCodeObj = areacodeRepository.findByAreaCode(areacode);
                // WeatherDTO_2 리스트 생성
                List<WeatherDTO_2> weatherList = parseAndMapToDTO_1(itemArray, areaCodeObj);

                url = baseUrl_2 +
                        "?serviceKey=" + apiKey +
                        "&pageNo=1&numOfRows=" + numOfRows +
                        "&dataType=JSON&regId=" + areaCodeObj.getRegId2() +
                        "&tmFc=" + tmFc;

                System.out.println("중기날씨(4 ~ 7일) 정보[기상] 요청 URL: " + url);

                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");

                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                sb = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();

                mapper = new ObjectMapper();
                jsonObject = mapper.readTree(sb.toString());

                response = jsonObject.get("response");
                body = response.get("body");
                items = body.get("items");
                itemArray = items.get("item");

                // WeatherDTO_2 리스트 생성
                weatherList = parseAndMapToDTO_2(weatherList, itemArray, lastCallApiDate, areaCodeObj);

                // WeatherDTO_2 리스트 저장 및 유효성 검사
                boolean allValuesPresent = saveWeatherList(weatherList, areacode, regId);

                // 값이 모두 존재할 때만 LastCallApiDate 저장
                if (allValuesPresent) {
                } else {
                    System.out.println("데이터 유효성 검사 실패로 LastCallApiDate 저장되지 않음.");
                }
            } else {
                System.out.println("중기날씨(4 ~ 7일) 이미 호출된 URL:" + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("중기날씨(4 ~ 7일) 저장 성공!");
    }


    @Override
    public List<WeatherDTO_2> getWeatherInfo_middle(Long areacode, String regId) {
        System.out.println("지역코드: " + areacode + "\n지역고유번호: " + regId);
        List<WeatherDTO_2> weatherList = weatherRepository_2.findWeatherByAreacodeAndRegId(areacode, regId);
        System.out.println("중기날씨(4 ~ 7일) 불러오기 성공!: " + weatherList);
        System.out.println();
        return weatherList;
    }

    private List<WeatherDTO_2> parseAndMapToDTO_1(JsonNode itemArray, Areacode areacode) {
        List<WeatherDTO_2> weatherList = new ArrayList<>();
        WeatherDTO_2 dto = new WeatherDTO_2();
        for (JsonNode item : itemArray) {
            dto.setAreacode(areacode);
            dto.setTmFc(tmFc1);
            dto.setTaMin4(item.get("taMin4").asText());
            dto.setTaMax4(item.get("taMax4").asText());
            dto.setTaMin5(item.get("taMin5").asText());
            dto.setTaMax5(item.get("taMax5").asText());
            dto.setTaMin6(item.get("taMin6").asText());
            dto.setTaMax6(item.get("taMax6").asText());
            dto.setTaMin7(item.get("taMin7").asText());
            dto.setTaMax7(item.get("taMax7").asText());
        }
        weatherList.add(dto);

        System.out.println("중기날씨(4 ~ 7일) [기온] 파싱결과: " + weatherList);
        return weatherList;
    }

    private List<WeatherDTO_2> parseAndMapToDTO_2(List<WeatherDTO_2> weatherList, JsonNode itemArray, LastCallApiDate LastCallApiDate, Areacode areacode) {

        for (WeatherDTO_2 dto : weatherList) {
            for (JsonNode item : itemArray) {
                        dto.setLastCallApiDate(LastCallApiDate);
                        dto.setWf4Am(item.get("wf4Am").asText());
                        dto.setWf4Pm(item.get("wf4Pm").asText());
                        dto.setWf5Am(item.get("wf5Am").asText());
                        dto.setWf5Pm(item.get("wf5Pm").asText());
                        dto.setWf6Am(item.get("wf6Am").asText());
                        dto.setWf6Pm(item.get("wf6Pm").asText());
                        dto.setWf7Am(item.get("wf7Am").asText());
                        dto.setWf7Pm(item.get("wf7Pm").asText());
                        dto.setRnSt4Am(item.get("rnSt4Am").asText());
                        dto.setRnSt4Pm(item.get("rnSt4Pm").asText());
                        dto.setRnSt5Am(item.get("rnSt5Am").asText());
                        dto.setRnSt5Pm(item.get("rnSt5Pm").asText());
                        dto.setRnSt6Am(item.get("rnSt6Am").asText());
                        dto.setRnSt6Pm(item.get("rnSt6Pm").asText());
                        dto.setRnSt7Am(item.get("rnSt7Am").asText());
                        dto.setRnSt7Pm(item.get("rnSt7Pm").asText());


            }
        }
        System.out.println("중기날씨(4 ~ 7일) [기상] 파싱결과: " + weatherList);
        return weatherList;
    }

    private boolean saveWeatherList(List<WeatherDTO_2> weatherList, Long areacode, String regId) {
        boolean allValuesPresent = true;

        for (WeatherDTO_2 weather : weatherList) {
            try {
                WeatherDTO_2 existingWeather = weatherRepository_2.findByAreacode(weather);
                if (existingWeather != null) {

                        weatherRepository_2.updateWeather_middle(weather);
                } else {
                        weatherRepository_2.insertWeather_middle(weather);
                }
            } catch (Exception e) {
                allValuesPresent = false;
                System.err.println("중기날씨 정보 저장 중 오류 발생: " + e.getMessage());
            }
        }

        return allValuesPresent;
    }
}



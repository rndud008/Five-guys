package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.LastCallApiData;
import com.lec.spring.domain.WeatherDTO_2;
import com.lec.spring.repository.AreacodeRepository;
import com.lec.spring.repository.LastCallApiDataRepository;
import com.lec.spring.repository.WeatherRepository_2;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherService_2Impl implements WeatherService_2 {

    @Value("${app.apikey}")
    private String apiKey;
    private String baseUrl_1 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa";
    private String baseUrl_2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
    private String numOfRows = "10";

    private WeatherRepository_2 weatherRepository_2;
    private AreacodeRepository areacodeRepository;
    private LastCallApiDataRepository lastCallApiDataRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'0600'");
    String tmFc = dateFormat.format(new Date());
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    String tmFc1 = dateFormat1.format(new Date());
    @Autowired
    public WeatherService_2Impl(SqlSession sqlSession) {
        weatherRepository_2 = sqlSession.getMapper(WeatherRepository_2.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
    }

    @Override
    @Transactional
    public void saveWeatherInfo_1(Long areacode, String regId) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl_1)
                .queryParam("serviceKey", apiKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", numOfRows)
                .queryParam("dataType", "JSON")
                .queryParam("regId", regId)
                .queryParam("tmFc", tmFc)
                .toUriString();
        System.out.println("saveWeatherInfo_1 요청 URL: " + url);

        try {
            LastCallApiData existingData = lastCallApiDataRepository.findByUrl(url);

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

                // areacode로 Areacode 객체 가져오기
                Areacode areaCodeObj = areacodeRepository.findByAreaCode(areacode);
                // WeatherDTO_2 리스트 생성
                List<WeatherDTO_2> weatherList = parseAndMapToDTO_1(itemArray, areaCodeObj);

                // WeatherDTO_2 리스트 저장 및 유효성 검사
                boolean allValuesPresent = saveWeatherList(weatherList, areacode, regId, "1");

                // 값이 모두 존재할 때만 LastCallApiData 저장
                if (allValuesPresent) {
                    // 마지막 호출 데이터 저장
                    LastCallApiData lastCallApiData = new LastCallApiData();
                    lastCallApiData.setUrl(url);
                    lastCallApiDataRepository.save(lastCallApiData);
                    System.out.println("LastCallApiData 저장됨: " + lastCallApiData);
                } else {
                    System.out.println("데이터 유효성 검사 실패로 LastCallApiData 저장되지 않음.");
                }

                System.out.println("Weather data to save: " + weatherList);
            } else {
                System.out.println("이미 호출됨: " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("saveWeatherInfo_1 finished execution");
    }

    @Override
    @Transactional
    public void saveWeatherInfo_2(Long areacode, String regId2) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl_2)
                .queryParam("serviceKey", apiKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", numOfRows)
                .queryParam("dataType", "JSON")
                .queryParam("regId", regId2)
                .queryParam("tmFc", tmFc)
                .toUriString();
        System.out.println("saveWeatherInfo_2 요청 URL: " + url);

        try {
            LastCallApiData existingData = lastCallApiDataRepository.findByUrl(url + areacode);

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
                LastCallApiData lastCallApiData = new LastCallApiData();
                // 이러면 중복호출되긴함..
                lastCallApiData.setUrl(url+ areacode);
                lastCallApiDataRepository.save(lastCallApiData);

                // areacode로 Areacode 객체 가져오기
                Areacode areaCodeObj = areacodeRepository.findByAreaCode(areacode);
                // WeatherDTO_2 리스트 생성
                List<WeatherDTO_2> weatherList = parseAndMapToDTO_2(itemArray, lastCallApiData, areaCodeObj);

                // WeatherDTO_2 리스트 저장
                for (WeatherDTO_2 weather : weatherList) {
                    try {
                        // LastCallApiData ID를 설정하여 WeatherDTO_2 객체에 저장
                        weather.setLastCallApiData(lastCallApiData);

                        WeatherDTO_2 existingWeather = weatherRepository_2.findByAreacode(weather);
                        if (existingWeather != null) {
                            weatherRepository_2.updateWeather_2(weather);
                        } else {
                            weatherRepository_2.insertWeather_2(weather);
                        }
                    } catch (Exception e) {
                        System.err.println("날씨 정보 저장 중 오류 발생: " + e.getMessage());
                    }
                }
                System.out.println("Weather data to save: " + weatherList);

            } else {
                System.out.println("이미 호출된 URL: " + url + areacode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<WeatherDTO_2> getWeatherInfo_1(Long areacode, String regId) {
        System.out.println("Fetching weather information for areacode: " + areacode + ", regId: " + regId);
        List<WeatherDTO_2> weatherList = weatherRepository_2.findWeatherByAreacodeAndRegId(areacode, regId);
        System.out.println("getWeatherInfo_1 성공" + weatherList);
        return weatherList;
    }

    @Override
    public List<WeatherDTO_2> getWeatherInfo_2(Long areacode, String regId2) {
        System.out.println("Fetching weather information for areacode: " + areacode + ", regId2: " + regId2);
        List<WeatherDTO_2> weatherList = weatherRepository_2.findWeatherByAreacodeAndRegId2(areacode, regId2);
        System.out.println("getWeatherInfo_2 성공" + weatherList);
        return weatherList;
    }

    private List<WeatherDTO_2> parseAndMapToDTO_1(JsonNode itemArray, Areacode areacode) {
        List<WeatherDTO_2> weatherList = new ArrayList<>();

        for (JsonNode item : itemArray) {
            WeatherDTO_2 dto = WeatherDTO_2.builder()
                    .areacode(areacode)
                    .tmFc(tmFc1)
                    .taMin4(item.get("taMin4").asText())
                    .taMax4(item.get("taMax4").asText())
                    .taMin5(item.get("taMin5").asText())
                    .taMax5(item.get("taMax5").asText())
                    .taMin6(item.get("taMin6").asText())
                    .taMax6(item.get("taMax6").asText())
                    .taMin7(item.get("taMin7").asText())
                    .taMax7(item.get("taMax7").asText())
                    .build();
            weatherList.add(dto);
        }

        System.out.println("parseAndMapToDTO_1 finished execution, result: " + weatherList);
        return weatherList;
    }

    private List<WeatherDTO_2> parseAndMapToDTO_2(JsonNode itemArray, LastCallApiData lastCallApiData, Areacode areacode) {
        List<WeatherDTO_2> weatherList = new ArrayList<>();

        for (JsonNode item : itemArray) {
            WeatherDTO_2 dto = WeatherDTO_2.builder()
                    .areacode(areacode)
                    .lastCallApiData(lastCallApiData )

                    .wf4Am(item.has("wf4Am") ? item.get("wf4Am").asText() : null)
                    .wf4Pm(item.has("wf4Pm") ? item.get("wf4Pm").asText() : null)
                    .wf5Am(item.has("wf5Am") ? item.get("wf5Am").asText() : null)
                    .wf5Pm(item.has("wf5Pm") ? item.get("wf5Pm").asText() : null)
                    .wf6Am(item.has("wf6Am") ? item.get("wf6Am").asText() : null)
                    .wf6Pm(item.has("wf6Pm") ? item.get("wf6Pm").asText() : null)
                    .wf7Am(item.has("wf7Am") ? item.get("wf7Am").asText() : null)
                    .wf7Pm(item.has("wf7Pm") ? item.get("wf7Pm").asText() : null)
                    .build();
            weatherList.add(dto);
        }

        System.out.println("parseAndMapToDTO_2 finished execution, result: " + weatherList);
        return weatherList;
    }

    private boolean saveWeatherList(List<WeatherDTO_2> weatherList, Long areacode, String regId, String type) {
        boolean allValuesPresent = true;

        for (WeatherDTO_2 weather : weatherList) {
            try {
                WeatherDTO_2 existingWeather = weatherRepository_2.findByAreacode(weather);
                if (existingWeather != null) {
                    if ("1".equals(type)) {
                        weatherRepository_2.updateWeather_1(weather);
                    } else if ("2".equals(type)) {
                        weatherRepository_2.updateWeather_2(weather);
                    }
                } else {
                    if ("1".equals(type)) {
                        weatherRepository_2.insertWeather_1(weather);
                    } else if ("2".equals(type)) {
                        weatherRepository_2.insertWeather_2(weather);
                    }
                }
            } catch (Exception e) {
                allValuesPresent = false;
                System.err.println("날씨 정보 저장 중 오류 발생: " + e.getMessage());
            }
        }

        return allValuesPresent;
    }
}



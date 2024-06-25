package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.LastCallApiData;
import com.lec.spring.domain.WeatherDTO;
import com.lec.spring.repository.AreacodeRepository;
import com.lec.spring.repository.LastCallApiDataRepository;
import com.lec.spring.repository.WeatherRepository;
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
public class WeatherServiceImpl implements WeatherService {

    @Value("${app.apikey}")
    private String apiKey;
    private String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    private String baseTime = "0200";
    private String numOfRows = "1000";
    private WeatherRepository weatherRepository;
    private AreacodeRepository areacodeRepository;
    private LastCallApiDataRepository lastCallApiDataRepository;

    // 현재 시간을 yyyyMMdd 형식으로 포맷팅
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String baseDate = dateFormat.format(new Date());

    @Autowired
    public WeatherServiceImpl(SqlSession sqlSession) {
        weatherRepository = sqlSession.getMapper(WeatherRepository.class);
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
    }

    @Override
    @Transactional
    public void saveWeatherInfo(Long areacode, int nx, int ny) {

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", apiKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", numOfRows)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .toUriString();
        System.out.println("saveWeatherInfo 요청 URL: " + url);

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

                // 마지막 호출 데이터 저장
                LastCallApiData lastCallApiData = new LastCallApiData();
                lastCallApiData.setUrl(url);
                lastCallApiDataRepository.save(lastCallApiData);

                // areacode로 Areacode 객체 가져오기
                Areacode areaCodeObj = areacodeRepository.findByAreaCode(areacode);
                // WeatherDTO 리스트 생성
                List<WeatherDTO> weatherList = parseAndMapToDTO(itemArray, lastCallApiData, areaCodeObj);

                // WeatherDTO 리스트 저장
                for (WeatherDTO weather : weatherList) {
                    try {
                        WeatherDTO existingWeather = weatherRepository.findByAreacodeFcstDateTime(weather);
                        if (existingWeather != null) {
                            weatherRepository.updateWeather(weather);
                        } else {
                            weatherRepository.insertWeather(weather);
                        }
                    } catch (Exception e) {
                        System.err.println("날씨 정보 저장 중 오류 발생: " + e.getMessage());
                    }
                }

            } else {
                System.out.println("이미 호출됨: " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("saveWeatherInfo 성공!");
    }

    @Override
    public List<WeatherDTO> getWeatherInfo(Long areacode, int nx, int ny) {
        List<WeatherDTO> weatherList = weatherRepository.findWeatherByAreacodeAndCoordinates(areacode, nx, ny);
        return weatherList;
    }

    // JSON 데이터를 파싱하고 WeatherDTO_2로 매핑하는 메서드
    private List<WeatherDTO> parseAndMapToDTO(JsonNode itemArray, LastCallApiData lastCallApiData, Areacode areacode) {
        Map<String, WeatherDTO> weatherMap = new HashMap<>();

        for (JsonNode data : itemArray) {

            String fcstDate = data.get("fcstDate").asText();
            String fcstTime = data.get("fcstTime").asText();
            String category = data.get("category").asText();
            String fcstValue = data.get("fcstValue").asText();

            // 예시로 특정 시간대의 데이터만 처리
            if (!"0600".equals(fcstTime) && !"1500".equals(fcstTime)) {
                continue; // 0600과 1500 시간이 아닌 경우 건너뜀
            }

            String key = fcstDate + fcstTime;
            WeatherDTO weather = weatherMap.getOrDefault(key, new WeatherDTO());
            weather.setAreacode(areacode);
            weather.setLastCallApiData(lastCallApiData);
            weather.setFcstDate(fcstDate);
            weather.setFcstTime(fcstTime);

            // 각 카테고리에 따라 값을 설정
            if ("TMN".equals(category)) {
                weather.setTMN(fcstValue);
            } else if ("TMX".equals(category)) {
                weather.setTMX(fcstValue);
            } else if ("SKY".equals(category)) {
                weather.setSKY(fcstValue);
            } else if ("POP".equals(category)) {
                weather.setPOP(fcstValue);
            } else if ("PTY".equals(category)) {
                weather.setPTY(fcstValue);
            }

            weatherMap.put(key, weather);
        }

        // TMN 또는 TMX 값이 설정되지 않은 경우 null로 설정
        for (WeatherDTO weather : weatherMap.values()) {
            if (weather.getTMN() == null) {
                weather.setTMN("null");
            }
            if (weather.getTMX() == null) {
                weather.setTMX("null");
            }
        }

        List<WeatherDTO> resultList = new ArrayList<>(weatherMap.values());
//        System.out.println("parseAndMapToDTO finished execution, result: " + resultList);
        return resultList;
    }

}

package com.lec.spring.service;

import com.lec.spring.domain.WeatherInfo;
import com.lec.spring.repository.WeatherInfoRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShortWeatherServiceImpl implements ShortWeatherService {

    private final WeatherInfoRepository weatherInfoRepository;

    @Autowired
    public ShortWeatherServiceImpl(WeatherInfoRepository weatherInfoRepository) {
        this.weatherInfoRepository = weatherInfoRepository;
    }

    @Override
    @Transactional
    public void saveShortWeatherForecastToDB(int nx, int ny, String baseDate, String baseTime) {
        List<WeatherInfo> weatherInfoList = getShortWeatherForecast(nx, ny, baseDate, baseTime);

        // WeatherInfo 데이터를 DB에 저장
        for (WeatherInfo weatherInfo : weatherInfoList) {
            weatherInfoRepository.insertWeatherInfo(weatherInfo);
        }
    }


    @Override
    public List<WeatherInfo> getShortWeatherForecast(int nx, int ny, String baseDate, String baseTime) {
        List<WeatherInfo> weatherInfoList = new ArrayList<>();

        try {
            /* URL construction */
            String serviceKey = "oshjO8WG9VLp87/CQQK/YzU9KWIOr/3VlA8jNBbi40aHpZM1RyvXyDNiCfF3IMl4wPg0UicSNMFHYNtQZVfzNQ==";
            String urlBuilder = String.format(
                    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"
                            + "serviceKey=%s"
                            + "&pageNo=1"
                            + "&numOfRows=1000"
                            + "&dataType=JSON"
                            + "&base_date=%s" // Use the baseDate parameter directly here
                            + "&base_time=%s"
                            + "&nx=%d"
                            + "&ny=%d",
                    serviceKey, baseDate, baseTime, nx, ny);

            URL url = new URL(urlBuilder);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

            /* JSON 데이터 파싱 */
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());

            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");

            JSONArray itemArray = (JSONArray) items.get("item");

            for (int i = 0; i < itemArray.size(); i++) {
                JSONObject data = (JSONObject) itemArray.get(i);
                String fcstTime = (String) data.get("fcstTime");
                String category = (String) data.get("category");

                // 필요한 시간대(0600 또는 1500)와 카테고리 필터링
                if (("0600".equals(fcstTime) || "1500".equals(fcstTime))
                        && ("tmn".equalsIgnoreCase(category) ||
                        "tmx".equalsIgnoreCase(category) ||
                        "sky".equalsIgnoreCase(category) ||
                        "pop".equalsIgnoreCase(category) ||
                        "pty".equalsIgnoreCase(category))) {

                    String fcstDate = (String) data.get("fcstDate");
                    String fcstValue = (String) data.get("fcstValue");

                    WeatherInfo weatherInfo = new WeatherInfo();
                    weatherInfo.setCategory(category);
                    weatherInfo.setFcstDate(fcstDate);
                    weatherInfo.setFcstTime(fcstTime);
                    weatherInfo.setFcstValue(fcstValue);
                    weatherInfoList.add(weatherInfo);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return weatherInfoList;
    }
}

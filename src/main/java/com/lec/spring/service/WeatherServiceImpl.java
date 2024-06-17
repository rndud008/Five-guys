package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lec.spring.domain.WeatherDTO;
import com.lec.spring.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {


    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }


    @Override
    @Transactional
    public void saveWeatherInfo() {
        String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = "oshjO8WG9VLp87/CQQK/YzU9KWIOr/3VlA8jNBbi40aHpZM1RyvXyDNiCfF3IMl4wPg0UicSNMFHYNtQZVfzNQ==";
        String baseTime = "0200";
        String nx = "60";
        String ny = "127";
        String numOfRows = "1000";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String baseDate = dateFormat.format(new Date());

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", numOfRows)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .toUriString();

        try {
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

            List<WeatherDTO> weatherList = parseAndMapToDTO(itemArray, url);

            for (WeatherDTO weather : weatherList) {
                weatherRepository.insertWeather(weather);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<WeatherDTO> parseAndMapToDTO(JsonNode itemArray, String url) {
        List<WeatherDTO> weatherList = new ArrayList<>();

        for (JsonNode data : itemArray) {
            String fcstTime = data.get("fcstTime").asText();
            if ("0600".equals(fcstTime) || "1500".equals(fcstTime)) {
                WeatherDTO weather = new WeatherDTO();
                weather.setCategory(data.get("category").asText());
                weather.setFcstDate(data.get("fcstDate").asText());
                weather.setFcstTime(fcstTime);
                weather.setFcstValue(data.get("fcstValue").asText());
                weather.setUrl(url);

                weatherList.add(weather);
            }
        }

        return weatherList;
    }
}
